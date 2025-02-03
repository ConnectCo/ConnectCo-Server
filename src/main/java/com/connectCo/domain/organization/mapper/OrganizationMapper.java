package com.connectCo.domain.organization.mapper;

import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationInquiryResponse;
import com.connectCo.domain.organization.dto.response.OrganizationSearchResponse;
import com.connectCo.domain.organization.entity.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {
    public Organization toOrganization(Member member, OrganizationCreateRequest request, Address address) {
        return Organization.builder()
            .name(request.getName())
            .phoneNumber(request.getOrganizationNumber())
            .email(request.getEmail())
            .address(address)
            .member(member)
            .profileType(ProfileType.ORGANIZATION)
            .build();
    }
//
//    public OrganizationInquiryResponse toOrganizationInquiryResponse(Organization organization) {
//        return OrganizationInquiryResponse.builder()
//                .organizationId(organization.getId())
//                .name(organization.getName())
//                .homepageUrl(organization.getHomepageUrl())
//                .academicDayUrl(organization.getAcademicDayUrl())
//                .build();
//    }

    public OrganizationSearchResponse toOrganizationSearchResponse(Organization organization) {
        return new OrganizationSearchResponse(organization.getId(), organization.getName());
    }
}
