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
import com.connectCo.global.common.enums.InquiryType;
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

    @Operation(summary = "쿠폰 조회 API(추천순, 거리순, 최근순)")
    @Parameters(value = {
            @Parameter(name = "type", description = "조회 타입 지정(추천순: RECOMMEND, 거리순: DISTANCE, 최근순: RECENT"),
            @Parameter(name = "latitude", description = "유저의 현재 위치의 위도(최근순의 경우 사용 X)"),
            @Parameter(name = "longitude", description = "유저의 현재 위치의 경도(최근순의 경우 사용 X)"),
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
            @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
    })
    @GetMapping("/recommend")
    public BaseResponse<CouponPagingResponse<CouponSummaryInquiryResponse>> inquiryCoupon(
            @RequestParam(name = "type") InquiryType type,
            @RequestParam(name = "latitude") Double latitude,
            @RequestParam(name = "longitude") Double longitude,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    ){
        return BaseResponse.onSuccess(couponService.inquiryCoupon(type, latitude, longitude, page , size));
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
