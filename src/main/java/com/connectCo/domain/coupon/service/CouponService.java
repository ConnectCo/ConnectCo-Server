package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.response.CouponInquiryByMemberResponse;

import java.util.List;

public interface CouponService {
    List<CouponInquiryByMemberResponse> inquiryCouponByMember();
}
