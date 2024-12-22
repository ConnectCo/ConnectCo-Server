package com.connectCo.domain.coupon.controller;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailResponse;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.service.CouponService;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    @Parameters(value = {
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
            @Parameter(name = "size", description = "한 페이지 당 쿠폰 개수"),
    })
    public BaseResponse<CouponPagingResponse<CouponSummaryInquiryResponse>> inquiryCouponByMember(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(couponService.inquiryCouponByMember(page, size));
    }

    @Operation(summary = "내가 찜한 쿠폰 조회 API")
    @GetMapping("/like")
    @Parameters(value = {
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
            @Parameter(name = "size", description = "한 페이지 당 쿠폰 개수"),
    })
    public BaseResponse<CouponPagingResponse<CouponSummaryInquiryResponse>> inquiryCouponByLike(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(couponService.inquiryCouponByLike(page, size));
    }

    @Operation(summary = "쿠폰 조회 API(최신순)")
    @GetMapping("/recent")
    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByRecent() {
        return BaseResponse.onSuccess(couponService.inquiryCouponByRecent());
    }
    @Operation(summary = "특정 가게의 쿠폰 조회 API")
    @GetMapping("/store/{storeId}")
    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByEachStore(@PathVariable Long storeId) {
        return BaseResponse.onSuccess(couponService.inquiryCouponByEachStore(storeId));
    }

    @Operation(summary = "쿠폰 상세 조회 API")
    @GetMapping("/{couponId}")
    public BaseResponse<CouponDetailResponse> inquiryCouponDetail(@PathVariable Long couponId) {
        return BaseResponse.onSuccess(couponService.inquiryCouponDetail(couponId));
    }


    @Operation(summary = "쿠폰 등록 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<CouponIdResponse> createCoupon(@RequestPart(value = "couponImages", required = false) List<MultipartFile> couponImages,
                                                       @RequestPart("request") @Valid CouponCreateRequest request) {
        return BaseResponse.onSuccess(couponService.createCoupon(couponImages, request));
    }

    @Operation(summary = "쿠폰 삭제 API")
    @DeleteMapping("/{couponId}")
    public BaseResponse<CouponIdResponse> deleteCoupon(@PathVariable Long couponId) {
        return BaseResponse.onSuccess(couponService.deleteCoupon(couponId));
    }

    @Operation(summary = "쿠폰 수정 API")
    @PutMapping(value = "/{couponId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<CouponIdResponse> updateCoupon(@PathVariable Long couponId,
                                                       @RequestPart(value = "couponImages", required = false) List<MultipartFile> couponImages,
                                                       @RequestPart("request") @Valid CouponCreateRequest request) {
        return BaseResponse.onSuccess(couponService.updateCoupon(couponId, couponImages, request));
    }
}
