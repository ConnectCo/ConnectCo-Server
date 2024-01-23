package com.connectCo.domain.coupon.controller;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.service.CouponService;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.response.StoreIdResponse;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "쿠폰 API", description = "쿠폰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "나의 쿠폰 조회 API")
    @GetMapping("/mine")
    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByMember() {
        return BaseResponse.onSuccess(couponService.inquiryCouponByMember());
    }

    @Operation(summary = "내가 찜한 쿠폰 조회 API")
    @GetMapping("/like")
    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByLike() {
        return BaseResponse.onSuccess(couponService.inquiryCouponByLike());
    }

    @Operation(summary = "쿠폰 조회 API(최신순)")
    @GetMapping("/recent")
    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByRecent() {
        return BaseResponse.onSuccess(couponService.inquiryCouponByRecent());
    }

    @Operation(summary = "쿠폰 등록 API")
    @PostMapping
    public BaseResponse<CouponIdResponse> createCoupon(@RequestPart(value = "couponImages", required = false) List<MultipartFile> couponImages,
                                                       @RequestPart("request") CouponCreateRequest request) {
        return BaseResponse.onSuccess(couponService.createCoupon(couponImages, request));
    }



}
