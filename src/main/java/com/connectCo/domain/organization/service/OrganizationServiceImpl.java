package com.connectCo.domain.organization.service;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.service.AddressService;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationDetailInquiryResponse;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.dto.response.OrganizationPagingResponse;
import com.connectCo.domain.organization.dto.response.OrganizationSummaryInquiryResponse;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.mapper.OrganizationMapper;
import com.connectCo.domain.organization.repository.OrganizationRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.global.validation.ParamValidator;
import com.connectCo.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationLikeService organizationLikeService;
    private final OrganizationMapper organizationMapper;
    private final AddressService addressService;
    private final S3FileComponent s3FileComponent;
    private final UnivCertClient univCertClient;

    @Override
    @Transactional
    public OrganizationIdResponse createOrganization(
        Member member, MultipartFile profileImage, OrganizationCreateRequest request
    ) {
        // 조직 등록 제한 검사
        if (organizationRepository.countByMember(member) >= 3) {
            throw new CustomApiException(ErrorCode.ORGANIZATION_LIMIT_EXCEEDED);
        }

        Address address = addressService.createAddress(
            request.getDetailAddress(), request.getLatitude(), request.getLongitude()
        );

        // TODO: 이메일 인증 로직 추가

        Organization newOrganization = createAndSaveOrganization(member, request, address);

        // 프로필 이미지 업로드
        if (profileImage != null) {
            String profileUrl = s3FileComponent.uploadFile("organization", profileImage);
            newOrganization.updateProfileImage(profileUrl);
        }

        return new OrganizationIdResponse(newOrganization.getId());
    }

    @Override
    @Transactional
    public OrganizationIdResponse updateOrganization(
        Member member, Long organizationId, MultipartFile profileImage, OrganizationUpdateRequest request
    ) {
        Organization organization = loadOrganization(organizationId);
        // 수정 권한 유효성 검사(본인이 아닌 경우 수정 불가)
        ParamValidator.validModify(organization.getMember().getId(), member.getId());
        if (organizationRepository.existsOrganizationByName(request.getName()) && !organization.getName().equals(request.getName())) {
            throw new CustomApiException(ErrorCode.ORGANIZATION_NAME_DUPLICATION);
        }

        // 주소 정보 업데이트
        organization.getAddress().updateAddress(
            request.getDetailAddress(), request.getLatitude(), request.getLongitude()
        );

        // 정보 수정
        organization.updateOrganizationInfo(request);

        // 프로필 이미지 업데이트(이미지가 있을 경우)
        if (profileImage != null) {
            if (organization.getProfileImage() != null) {
                s3FileComponent.deleteFile(organization.getProfileImage());
            }
            String profileUrl = s3FileComponent.uploadFile("organization", profileImage);
            organization.updateProfileImage(profileUrl);
        }

        return new OrganizationIdResponse(organization.getId());
    }

    @Override
    @Transactional
    public OrganizationIdResponse deleteOrganization(Member member, Long organizationId) {
        Organization organization = loadOrganization(organizationId);

        ParamValidator.validModify(member.getId(), organization.getMember().getId());

        // TODO 관련된 이벤트, 찜 기록, 협찬 기록, 채팅 기록 등 삭제 로직 추가

        organization.delete();
        s3FileComponent.deleteFile(organization.getProfileImage());
        organization.updateProfileImage(null);

        return new OrganizationIdResponse(organizationId);
    }

    @Override
    @Transactional
    public Boolean likeOrganization(Long storeId, Long organizationId) {
        Organization organization = loadOrganization(organizationId);
        return organizationLikeService.likeOrganization(storeId, organization);
    }

    @Override
    public OrganizationDetailInquiryResponse inquiryOrganizationDetail(
        Long profileId, ProfileType profileType, Long organizationId
    ) {
        // 본인 여부 확인
        Boolean isMine = organizationId.equals(profileId);
        Organization organization = loadOrganization(organizationId);

        // 찜 여부 확인
        Boolean isLiked = Boolean.FALSE;
        if (profileId != null && profileType == ProfileType.ORGANIZATION) {
            isLiked = organizationLikeService.isLikeOrganization(profileId, organization);
        }

        return organizationMapper.toOrganizationDetailInquiryResponse(
            organization, isLiked, isMine,
            organization.getEvents().stream().limit(2).map(organizationMapper::toOrganizationEvent).toList()
        );
    }

    @Override
    public OrganizationPagingResponse<OrganizationSummaryInquiryResponse> inquiryOrganizationsByLike(
        Long profileId, int page, int size
    ) {
        Page<Organization> organizationPage = organizationLikeService.getOrganizationsByLike(profileId, page, size);

        return organizationMapper.tOrganizationPagingResponse(
            organizationPage.map(organizationMapper::toOrganizationSummaryInquiryResponse)
        );
    }

    @Override
    public Organization loadOrganization(Long organizationId) {
        return organizationRepository.getOrganization(organizationId);
    }

    @Override
    @Transactional
    public boolean requestUniversityVerification(Long organizationId, String email) {
        Organization organization = loadOrganization(organizationId);
        
        // 이미 인증된 경우 예외 처리
        if (organization.isVerified()) {
            throw new CustomApiException(ErrorCode.UNIVERSITY_ALREADY_VERIFIED);
        }
        
        // UnivCert API 호출하여 인증 코드 발송
        boolean success = univCertClient.requestVerification(email, organization.getUniversityName());
        
        if (success) {
            // 이메일 업데이트 및 상태 변경
            organization.setEmail(email);
            organization.setPendingVerification(organization.getUniversityName());
        } else {
            throw new CustomApiException(ErrorCode.UNIVERSITY_API_ERROR);
        }
        
        return success;
    }
    
    @Override
    @Transactional
    public boolean verifyUniversityCode(Long organizationId, int code) {
        Organization organization = loadOrganization(organizationId);
        
        // 인증 코드 확인
        boolean success = univCertClient.verifyCode(
                organization.getEmail(), 
                organization.getUniversityName(), 
                code
        );
        
        if (!success) {
            throw new CustomApiException(ErrorCode.UNIVERSITY_CODE_INVALID);
        }
        
        // 인증 상태 확인
        UnivCertClient.VerificationResult result = 
                univCertClient.checkVerificationStatus(organization.getEmail());
        
        if (result.isVerified()) {
            organization.setVerified(result.getCertifiedDate());
            return true;
        } else {
            throw new CustomApiException(ErrorCode.UNIVERSITY_VERIFICATION_FAILED);
        }
    }
    
    @Override
    public UniversityVerificationStatus getVerificationStatus(Long organizationId) {
        Organization organization = loadOrganization(organizationId);
        
        // 이미 인증된 경우 API를 통해 재확인
        if (organization.getVerificationStatus() == UniversityVerificationStatus.VERIFIED) {
            UnivCertClient.VerificationResult result = 
                    univCertClient.checkVerificationStatus(organization.getEmail());
            
            if (!result.isVerified()) {
                organization.setPendingVerification(organization.getUniversityName());
            }
        }
        
        return organization.getVerificationStatus();
    }

    private Organization createAndSaveOrganization(Member member, OrganizationCreateRequest request, Address address) {
        Organization organization = organizationMapper.toOrganization(member, request, address);
        return organizationRepository.save(organization);
    }
}
