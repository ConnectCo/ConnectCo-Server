package com.connectCo.domain.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CouponIdResponse {
    private UUID couponId;
}
