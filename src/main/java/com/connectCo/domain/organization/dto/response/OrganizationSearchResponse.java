package com.connectCo.domain.organization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrganizationSearchResponse {
    private Long organizationId;
    private String name;
}
