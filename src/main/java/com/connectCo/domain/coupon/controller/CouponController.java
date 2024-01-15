package com.connectCo.domain.coupon.controller;

import com.connectCo.domain.coupon.dto.response.CouponInquiryByMemberResponse;
import com.connectCo.domain.coupon.service.CouponService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "쿠폰 API", description = "쿠폰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "나의 쿠폰 조회 API")
    @GetMapping("/member")
    public BaseResponse<List<CouponInquiryByMemberResponse>> inquiryCouponByMember() {
        return null;
    }

}
