package com.connectCo.domain.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "조직 설명", example = "이것은 조직 A 입니다.")
    @NotBlank(message = "조직 설명은 필수 입력값입니다.")
    private String description;

    @Schema(description = "조직 주소", example = "주소")
    @NotBlank(message = "조직 주소는 필수 입력값입니다.")
    private String detailAddress;

    @Schema(description = "위도", example = "37.5665")
    private double latitude;

    @Schema(description = "경도", example = "126.9780")
    private double longitude;

    @Schema(description = "조직 전화번호", example = "010-1234-5678")
    private String phoneNumber;
}
