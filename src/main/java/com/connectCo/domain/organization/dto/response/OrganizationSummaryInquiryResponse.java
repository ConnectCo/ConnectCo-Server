package com.connectCo.domain.organization.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationSummaryInquiryResponse {
    @Schema(description = "조직 ID", example = "1")
    private Long organizationId;

    @Schema(description = "조직 이름", example = "조직 A")
    private String name;

    @Schema(description = "조직 설명", example = "이것은 조직 A 입니다.")
    private String description;

    @Schema(description = "조직 썸네일 URL", example = "thumbnail-url")
    private String thumbnail;

    @Schema(description = "신청 가능한 이벤트 개수", example = "5")
    private long eventCount;
}
