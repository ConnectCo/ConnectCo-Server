package com.connectCo.domain.organization.dto.request;

import com.connectCo.domain.organization.entity.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationUpdateRequest {
    private String name;
    private String homepageUrl;
    private String academicDayUrl;
}
