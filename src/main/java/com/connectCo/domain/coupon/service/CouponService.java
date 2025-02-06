package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailResponse;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.entity.Store;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CouponService {
    CouponIdResponse createCoupon(
        Long profileId, List<MultipartFile> couponImages, CouponCreateRequest request
    );
//
//
//    CouponIdResponse deleteCoupon(Long couponId);
//
//    CouponIdResponse updateCoupon(Long couponId, @Nullable List<MultipartFile> couponImages, CouponCreateRequest request);
//
//
//
    List<CouponSummaryInquiryResponse> inquiryCouponByMember(Long profileId);
//    List<CouponSummaryInquiryResponse> inquiryCouponByLike();
//    List<CouponSummaryInquiryResponse> inquiryCouponByRecent();
//    List<CouponSummaryInquiryResponse> inquiryCouponByEachStore(Long storeId);
//
//    List<Coupon> inquiryCouponByStore(Store store);
//
//    CouponDetailResponse inquiryCouponDetail(Long couponId);

}
