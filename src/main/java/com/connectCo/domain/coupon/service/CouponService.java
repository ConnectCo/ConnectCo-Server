package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.request.CouponUpdateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailInquiryResponse;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.member.entity.ProfileType;
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
    Boolean likeCoupon(Long profileId, Long couponId);
    CouponDetailInquiryResponse inquiryCouponDetail(
        Long profileId, ProfileType profileType, Long couponId
    );
    CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponsByLike(Long profileId, int page, int size);
    List<CouponSummaryInquiryResponse> inquiryMyCoupons(Long profileId);
    CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponsByStore(Long storeId, int page, int size);
    CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponsByRecent(int page, int size);
}
