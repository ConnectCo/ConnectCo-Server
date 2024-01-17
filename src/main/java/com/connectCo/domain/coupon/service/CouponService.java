package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;

import java.util.List;

public interface CouponService {
    List<CouponSummaryInquiryResponse> inquiryCouponByMember();
    List<CouponSummaryInquiryResponse> inquiryCouponByLike();
}
