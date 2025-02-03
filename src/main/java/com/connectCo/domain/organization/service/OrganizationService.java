package com.connectCo.domain.organization.service;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.dto.response.OrganizationInquiryResponse;
import com.connectCo.domain.organization.dto.response.OrganizationSearchResponse;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.global.common.dto.AddressRequest;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface OrganizationService {
    OrganizationIdResponse createOrganization(
        Member member, MultipartFile profileImage, OrganizationCreateRequest request);
//    OrganizationIdResponse updateOrganizationInfo(Long organizationId, OrganizationUpdateRequest request);
//    OrganizationIdResponse updateOrganizationAddress(Long organizationId, AddressRequest request);
//    OrganizationIdResponse deleteOrganization(Long organizationId);
//    OrganizationInquiryResponse inquiryOrganization(String organizationName);
//    List<OrganizationSearchResponse> searchOrganization(String keyword);
//    Organization loadOrganization(Long organizationId);
//    Organization loadOrganizationByName(String name);
}
