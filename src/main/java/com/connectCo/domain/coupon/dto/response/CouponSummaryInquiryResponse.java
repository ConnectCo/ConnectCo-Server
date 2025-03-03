package com.connectCo.domain.coupon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CouponSummaryInquiryResponse {
    @Schema(description = "쿠폰 ID", example = "1")
    private Long id;

    @Schema(description = "가게 이름", example = "가게 A")
    private String name;

    @Schema(description = "쿠폰 이름", example = "쿠폰 A")
    private String title;

    @Schema(description = "쿠폰 신청 마감일", example = "2021-12-31")
    private LocalDate expiredAt;

    @Schema(description = "쿠폰 썸네일 URL", example = "thumbnail-url")
    private String thumbnail;
}
