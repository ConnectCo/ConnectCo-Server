package com.connectCo.domain.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationUpdateRequest {
    @NotBlank(message = "조직 이름은 필수 입력값입니다.")
    private String name;
    private String homepageUrl;
    private String academicDayUrl;
}
