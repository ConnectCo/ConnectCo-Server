package com.connectCo.domain.organization.mapper;

import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.entity.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {
    public Organization toOrganization(OrganizationCreateRequest request, Address address) {
        return Organization.builder()
                .name(request.getName())
                .organizationType(request.getType())
                .address(address)
                .homepageUrl(request.getHomepageUrl())
                .academicDayUrl(request.getAcademicDayUrl())
                .build();
    }
}
