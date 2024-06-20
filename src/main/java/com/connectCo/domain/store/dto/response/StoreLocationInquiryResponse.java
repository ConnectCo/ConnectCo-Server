package com.connectCo.domain.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreLocationInquiryResponse {
    @Schema(description = "가게 ID", example = "1")
    private Long storeId;

    @Schema(description = "가게 이름", example = "가게 A")
    private String name;

    @Schema(description = "가게 설명", example = "이것은 가게 A 입니다.")
    private String description;

    @Schema(description = "가게 썸네일 URL", example = "thumbnail-url")
    private String thumbnail;

    @Schema(description = "보유 쿠폰 수", example = "5")
    private int couponCount;

    @Schema(description = "가게 위치의 위도", example = "37.7749")
    private double latitude;

    @Schema(description = "가게 위치의 경도", example = "-122.4194")
    private double longitude;

    @Schema(description = "사용자 위치로부터의 거리", example = "2.5")
    private double distance;
}
