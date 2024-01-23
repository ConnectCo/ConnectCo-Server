package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.response.StoreIdResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CouponService {
    List<CouponSummaryInquiryResponse> inquiryCouponByMember();
    List<CouponSummaryInquiryResponse> inquiryCouponByLike();
    List<CouponSummaryInquiryResponse> inquiryCouponByRecent();

    CouponIdResponse createCoupon(List<MultipartFile> couponImages, CouponCreateRequest request);


}
