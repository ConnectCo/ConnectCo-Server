package com.connectCo.domain.coupon.controller;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.request.CouponUpdateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailInquiryResponse;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.service.CouponService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
        @Parameter(description = "쿠폰 생성 요청 json") @RequestPart("request") @Valid CouponCreateRequest request
    ) {
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
        @Parameter(description = "쿠폰 수정 요청 json") @RequestPart("request") @Valid CouponUpdateRequest request
    ) {
        return BaseResponse.onSuccess(
            couponService.updateCoupon(principal.profileId(), couponId, couponImages, request)
        );
    }

    @Operation(summary = "쿠폰 삭제 API", description = "가게 프로필만 삭제 가능")
    @DeleteMapping("/{couponId}")
    public BaseResponse<CouponIdResponse> deleteCoupon(
        @AuthenticationPrincipal PrincipalDetails principal,
        @Parameter(description = "삭제할 쿠폰 id") @PathVariable Long couponId
    ) {
        return BaseResponse.onSuccess(couponService.deleteCoupon(principal.profileId(), couponId));
    }

    @Operation(summary = "쿠폰 찜하기 API", description = "조직 프로필만 가능")
    @PostMapping("/{couponId}/like")
    public BaseResponse<Boolean> likeCoupon(
        @AuthenticationPrincipal PrincipalDetails principal,
        @Parameter(description = "찜할 쿠폰 id") @PathVariable Long couponId
    ) {
        return BaseResponse.onSuccess(couponService.likeCoupon(principal.profileId(), couponId));
    }


    @Operation(summary = "쿠폰 상세 조회 API", description = "비로그인 시도 가능")
    @GetMapping("/{couponId}/detail")
    public BaseResponse<CouponDetailInquiryResponse> inquiryCouponDetail(
        @AuthenticationPrincipal PrincipalDetails principal,
        @Parameter(description = "조회할 쿠폰 id") @PathVariable Long couponId
    ) {
        if (principal == null) {
            return BaseResponse.onSuccess(couponService.inquiryCouponDetail(null, null, couponId));
        }
        return BaseResponse.onSuccess(couponService.inquiryCouponDetail(
            principal.profileId(), principal.profileType(), couponId)
        );
    }

    @Operation(summary = "내가 찜한 쿠폰 조회 API", description = "조직 프로필만 가능")
    @Parameters(value = {
        @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
        @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
    })
    @GetMapping("/like")
    public BaseResponse<CouponPagingResponse<CouponSummaryInquiryResponse>> inquiryCouponByLike(
        @AuthenticationPrincipal PrincipalDetails principal,
        @RequestParam int page,
        @RequestParam int size
    ) {
        return BaseResponse.onSuccess(couponService.inquiryCouponsByLike(principal.profileId(), page, size));
    }

    @Operation(summary = "나의 쿠폰 조회 API", description = "가게 프로필만 조회 가능")
    @GetMapping("/mine")
    public BaseResponse<List<CouponSummaryInquiryResponse>> inquiryCouponByMember(
        @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return BaseResponse.onSuccess(couponService.inquiryMyCoupons(principal.profileId()));
    }

    @Operation(summary = "특정 가게의 쿠폰 조회 API", description = "비로그인 시도 가능")
    @Parameters(value = {
        @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
        @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
    })
    @GetMapping("/store/{storeId}")
    public BaseResponse<CouponPagingResponse<CouponSummaryInquiryResponse>> inquiryCouponByStore(
        @PathVariable Long storeId,
        @RequestParam int page,
        @RequestParam int size
    ) {
        return BaseResponse.onSuccess(couponService.inquiryCouponsByStore(storeId, page, size));
    }

    // TODO: 생성순, 거리순, 임박순 하나로 합치기? --> 생성순, 임박순도 결국 위치 기반으로 조회 해야 함(비로그인 시)
    @Operation(summary = "쿠폰 목록 조회 API(생성순)", description = "비로그인 시도 가능")
    @GetMapping("/recent")
    @Parameters(value = {
        @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
        @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
    })
    public BaseResponse<CouponPagingResponse<CouponSummaryInquiryResponse>> inquiryCouponByRecent(
        @RequestParam int page,
        @RequestParam int size
    ) {
        return BaseResponse.onSuccess(couponService.inquiryCouponsByRecent(page, size));
    }


    // TODO: 위치에 따른 쿠폰 조회 추가
    @Operation(summary = "쿠폰 목록 조회 API(거리순)", description = "비로그인 시도 가능(비로그인 시 현재 위치 기준)")
    @GetMapping("/distance")
    @Parameters(value = {
        @Parameter(name = "latitude", description = "위도, 비로그인 시만 사용"),
        @Parameter(name = "longitude", description = "경도, 비로그인 시만 사용"),
        @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
        @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
    })
    public BaseResponse<CouponPagingResponse<CouponSummaryInquiryResponse>> inquiryCouponByDistance(
        @AuthenticationPrincipal PrincipalDetails principal,
        @RequestParam(required = false) double latitude,
        @RequestParam(required = false) double longitude,
        @RequestParam int page,
        @RequestParam int size
    ) {
        return BaseResponse.onSuccess(null);
    }

    // TODO: 신청마감일 임박한 쿠폰 조회 추가
    @Operation(summary = "쿠폰 목록 조회 API(신청마감일 임박순)", description = "비로그인 시도 가능")
    @GetMapping("/deadline")
    @Parameters(value = {
        @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
        @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
    })
    public BaseResponse<CouponPagingResponse<CouponSummaryInquiryResponse>> inquiryCouponByDeadline(
        @RequestParam int page,
        @RequestParam int size
    ) {
        return BaseResponse.onSuccess(null);
    }

    // TODO: 쿠폰 추천 조회 추가
}
