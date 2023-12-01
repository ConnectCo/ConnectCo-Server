package com.connectCo.domain.coupon.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CouponType {
    VALID_COUNT("사용 횟수 제한 쿠폰"),
    VALID_PERIOD("사용 기간 제한 쿠폰"),
    VALID_DATE("사용 날짜 제한 쿠폰");

    private final String description;
}
