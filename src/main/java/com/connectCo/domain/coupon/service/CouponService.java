package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailResponse;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.global.common.enums.InquiryType;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CouponService {
    CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponByMember(int page, int size);
    CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponByLike(int page, int size);
    CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCoupon(InquiryType type, double latitude, double longitude, int page, int size);
    CouponDetailResponse inquiryCouponDetail(Long couponId);

    CouponIdResponse createCoupon(List<MultipartFile> couponImages, CouponCreateRequest request);

    CouponIdResponse deleteCoupon(Long couponId);

    CouponIdResponse updateCoupon(Long couponId, @Nullable List<MultipartFile> couponImages, CouponCreateRequest request);
}
