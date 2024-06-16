package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.entity.Store;

import java.util.List;

public interface CouponService {
    List<CouponSummaryInquiryResponse> inquiryCouponByMember();
    List<CouponSummaryInquiryResponse> inquiryCouponByLike();
    List<Coupon> inquiryCouponByStore(Store store);
}
