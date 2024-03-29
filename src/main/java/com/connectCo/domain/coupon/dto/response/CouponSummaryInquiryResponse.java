package com.connectCo.domain.coupon.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class CouponSummaryInquiryResponse {
    private UUID couponId;
    private String name;
    private String description;
    private LocalDate expiredAt;
    private String thumbnail;
}
