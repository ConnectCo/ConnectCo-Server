package com.connectCo.domain.coupon.mapper;

import com.connectCo.domain.coupon.dto.response.CouponInquiryByMemberResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponInquiryByMemberResponse toCouponInquiryByMemberResponse(Coupon coupon) {
        return CouponInquiryByMemberResponse.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .description(coupon.getDescription())
                .expiredAt(coupon.getExpiredAt())
                .build();
    }
}
