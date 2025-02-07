package com.connectCo.domain.organization.mapper;

import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationSearchResponse;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.entity.OrganizationLike;
import com.connectCo.domain.store.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {
    public Organization toOrganization(Member member, OrganizationCreateRequest request, Address address) {
        return Organization.builder()
            .name(request.getName())
            .phoneNumber(request.getPhoneNumber())
            .email(request.getEmail())
            .address(address)
            .member(member)
            .profileType(ProfileType.ORGANIZATION)
            .build();
    }

    public OrganizationLike toOrganizationLike(Organization organization, Store store) {
        return OrganizationLike.builder()
            .organization(organization)
            .store(store)
            .isActive(true)
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
