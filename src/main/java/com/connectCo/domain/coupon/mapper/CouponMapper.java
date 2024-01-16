package com.connectCo.domain.coupon.mapper;

import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponSummaryInquiryResponse toCouponSummaryInquiryResponse(Coupon coupon) {
        return CouponSummaryInquiryResponse.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .description(coupon.getDescription())
                .expiredAt(coupon.getExpiredAt())
                .build();
    }
}
