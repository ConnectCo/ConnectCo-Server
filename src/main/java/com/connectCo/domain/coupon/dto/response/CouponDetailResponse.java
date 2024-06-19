package com.connectCo.domain.coupon.dto.response;

import com.connectCo.domain.coupon.entity.CouponType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CouponDetailResponse {

    private Long id;
    private Long storeId;
    private String name;
    private String description;
    private String priorityTarget;
    private String notification;
    private String couponType;
    private LocalDate expiredAt;
    private List<String> images;
    private int validCount;
    private int validPeriod;
    private LocalDate validDate;

}
