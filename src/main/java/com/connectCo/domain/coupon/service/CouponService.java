package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import org.springframework.web.multipart.MultipartFile;
import com.connectCo.domain.store.entity.Store;

import java.util.List;

public interface CouponService {
    List<CouponSummaryInquiryResponse> inquiryCouponByMember();
    List<CouponSummaryInquiryResponse> inquiryCouponByLike();
    List<CouponSummaryInquiryResponse> inquiryCouponByRecent();
    CouponIdResponse createCoupon(List<MultipartFile> couponImages, CouponCreateRequest request);
    List<Coupon> inquiryCouponByStore(Store store);
}
