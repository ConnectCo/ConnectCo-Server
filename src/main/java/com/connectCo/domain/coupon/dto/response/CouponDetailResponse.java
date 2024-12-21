package com.connectCo.domain.coupon.dto.response;

import com.connectCo.domain.coupon.entity.CouponType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CouponDetailResponse {

    private Long id;
    private Long storeId;
    private String storeName;
    private String name;
    private String description;
    private String priorityTarget;
    private String notification;
    private LocalDate expiredAt;
    private LocalDateTime createdAt;
    private List<String> images;
}
