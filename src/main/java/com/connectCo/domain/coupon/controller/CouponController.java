package com.connectCo.domain.coupon.controller;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.request.CouponUpdateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailResponse;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.service.CouponService;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "쿠폰 API", description = "쿠폰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "쿠폰 등록 API", description = "가게 프로필만 등록 가능")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<CouponIdResponse> createCoupon(
        @AuthenticationPrincipal PrincipalDetails principal,
        @Parameter(description = "쿠폰 이미지 파일들(없을 시 사용 x)") @RequestPart(value = "couponImages", required = false) List<MultipartFile> couponImages,
        @Parameter(description = "쿠폰 생성 요청 json") @RequestPart("request") @Valid CouponCreateRequest request) {
        return BaseResponse.onSuccess(
            couponService.createCoupon(principal.profileId(), couponImages, request)
        );
    }

    @Operation(summary = "쿠폰 수정 API", description = "가게 프로필만 수정 가능")
    @PutMapping(value = "/{couponId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<CouponIdResponse> updateCoupon(
        @AuthenticationPrincipal PrincipalDetails principal,
        @Parameter(description = "수정할 쿠폰 id") @PathVariable Long couponId,
        @Parameter(description = "추가된 쿠폰 이미지 파일들(없을 시 사용 x)") @RequestPart(value = "couponImages", required = false) List<MultipartFile> couponImages,
        @Parameter(description = "쿠폰 수정 요청 json") @RequestPart("request") @Valid CouponUpdateRequest request) {
        return BaseResponse.onSuccess(
            couponService.updateCoupon(principal.profileId(), couponId, couponImages, request)
        );
    }
//
//    @Operation(summary = "쿠폰 삭제 API")
//    @DeleteMapping("/{couponId}")
//    public BaseResponse<CouponIdResponse> deleteCoupon(@PathVariable Long couponId) {
//        return BaseResponse.onSuccess(couponService.deleteCoupon(couponId));
//    }
//

    @Operation(summary = "나의 쿠폰 조회 API", description = "가게 프로필만 조회 가능")
    @GetMapping("/mine")
    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByMember(
        @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return BaseResponse.onSuccess(couponService.inquiryCouponByMember(principal.profileId()));
    }
//
//    @Operation(summary = "내가 찜한 쿠폰 조회 API")
//    @GetMapping("/like")
//    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByLike() {
//        return BaseResponse.onSuccess(couponService.inquiryCouponByLike());
//    }
//
//    @Operation(summary = "쿠폰 조회 API(최신순)")
//    @GetMapping("/recent")
//    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByRecent() {
//        return BaseResponse.onSuccess(couponService.inquiryCouponByRecent());
//    }
//    @Operation(summary = "특정 가게의 쿠폰 조회 API")
//    @GetMapping("/store/{storeId}")
//    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByEachStore(@PathVariable Long storeId) {
//        return BaseResponse.onSuccess(couponService.inquiryCouponByEachStore(storeId));
//    }
//
//    @Operation(summary = "쿠폰 상세 조회 API")
//    @GetMapping("/{couponId}/detail")
//    public BaseResponse<CouponDetailResponse> inquiryCouponDetail(@PathVariable Long couponId) {
//        return BaseResponse.onSuccess(couponService.inquiryCouponDetail(couponId));
//    }
//


}
