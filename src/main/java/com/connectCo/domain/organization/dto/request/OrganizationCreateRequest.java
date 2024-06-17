package com.connectCo.domain.organization.dto.request;

import com.connectCo.domain.organization.entity.OrganizationType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationCreateRequest {
    private String name;
    private OrganizationType type;
    private String detailAddress;
    private double latitude;
    private double longitude;
    private String homepageUrl;
    private String academicDayUrl;
}
