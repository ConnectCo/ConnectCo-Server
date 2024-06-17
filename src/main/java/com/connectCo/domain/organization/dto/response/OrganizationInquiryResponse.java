package com.connectCo.domain.organization.dto.response;

import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrganizationInquiryResponse {
    private Long organizationId;
    private String name;
    private String homepageUrl;
    private String academicDayUrl;
}
