package com.connectCo.domain.organization.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationInquiryResponse {
    private Long organizationId;
    private String name;
    private String homepageUrl;
    private String academicDayUrl;
}
