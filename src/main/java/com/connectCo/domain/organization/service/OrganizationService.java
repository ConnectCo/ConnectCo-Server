package com.connectCo.domain.organization.service;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationDetailInquiryResponse;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.dto.response.OrganizationPagingResponse;
import com.connectCo.domain.organization.dto.response.OrganizationSummaryInquiryResponse;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.entity.UniversityVerificationStatus;

import org.springframework.web.multipart.MultipartFile;

public interface OrganizationService {
    OrganizationIdResponse createOrganization(
        Member member, MultipartFile profileImage, OrganizationCreateRequest request
    );
    OrganizationIdResponse updateOrganization(
        Member member, Long organizationId, MultipartFile profileImage, OrganizationUpdateRequest request
    );
    OrganizationIdResponse deleteOrganization(Member member, Long organizationId);
    Boolean likeOrganization(Long storeId, Long organizationId);
    OrganizationDetailInquiryResponse inquiryOrganizationDetail(
        Long profileId, ProfileType profileType, Long organizationId
    );
    OrganizationPagingResponse<OrganizationSummaryInquiryResponse> inquiryOrganizationsByLike(
        Long profileId, int page, int size
    );
    Organization loadOrganization(Long organizationId);
    boolean requestUniversityVerification(Long organizationId, String email);
    boolean verifyUniversityCode(Long organizationId, int code);
    UniversityVerificationStatus getVerificationStatus(Long organizationId);
}
