package com.connectCo.domain.coupon.mapper;

import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.entity.CouponImage;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponSummaryInquiryResponse toCouponSummaryInquiryResponse(Coupon coupon) {
        String thumbnail = coupon.getImages().stream()
                .findFirst()
                .map(CouponImage::getUrl)
                .orElse(null);

        return CouponSummaryInquiryResponse.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .description(coupon.getDescription())
                .expiredAt(coupon.getExpiredAt())
                .thumbnail(thumbnail)
                .build();
    }
}
