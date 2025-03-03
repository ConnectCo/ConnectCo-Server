package com.connectCo.domain.coupon.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponSearchType {
    DISTANCE( "거리순"),
    RECENCY("최신순"),
    DEADLINE("마감임박순");

    private final String toKorean;
}
