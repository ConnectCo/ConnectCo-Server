package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.request.CouponUpdateRequest;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CouponService {
    CouponIdResponse createCoupon(
        Long profileId, List<MultipartFile> couponImages, CouponCreateRequest request
    );
    CouponIdResponse updateCoupon(
        Long profileId, Long couponId, List<MultipartFile> couponImages, CouponUpdateRequest request
    );
    CouponIdResponse deleteCoupon(Long profileId, Long couponId);

    List<CouponSummaryInquiryResponse> inquiryCouponByStore(Long profileId);
    CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponByLike(Long profileId, int page, int size);
//    List<CouponSummaryInquiryResponse> inquiryCouponByRecent();
//    List<CouponSummaryInquiryResponse> inquiryCouponByEachStore(Long storeId);
//
//    List<Coupon> inquiryCouponByStore(Store store);
//
//    CouponDetailResponse inquiryCouponDetail(Long couponId);

}
