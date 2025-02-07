package com.connectCo.domain.organization.service;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.service.AddressService;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationDetailInquiryResponse;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.mapper.OrganizationMapper;
import com.connectCo.domain.organization.repository.OrganizationRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.global.validation.ParamValidator;
import com.connectCo.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
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

//    @Override
//    public List<OrganizationSearchResponse> searchOrganization(String keyword) {
//
//        List<Organization> organizations = organizationRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(keyword);
//
//        return organizations.stream().map(organizationMapper::toOrganizationSearchResponse).toList();
//    }

    @Override
    public Organization loadOrganization(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.ORGANIZATION_NOT_FOUND));
    }
//
//    @Override
//    public Organization loadOrganizationByName(String name) {
//        return organizationRepository.findOrganizationByName(name)
//                .orElseThrow(() -> new CustomApiException(ErrorCode.ORGANIZATION_NOT_FOUND));
//    }

    private Organization createAndSaveOrganization(Member member, OrganizationCreateRequest request, Address address) {
        Organization organization = organizationMapper.toOrganization(member, request, address);
        return organizationRepository.save(organization);
    }
}
