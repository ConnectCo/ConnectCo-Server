package com.connectCo.domain.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreSummaryInquiryResponse {
    @Schema(description = "가게 ID", example = "1")
    private Long storeId;

    @Schema(description = "가게 이름", example = "가게 A")
    private String name;

    @Schema(description = "가게 설명", example = "이것은 가게 A 입니다.")
    private String description;

    @Schema(description = "가게 썸네일 URL", example = "thumbnail-url")
    private String thumbnail;

    @Schema(description = "보유 쿠폰 개수", example = "5")
    private int couponCount;
}
