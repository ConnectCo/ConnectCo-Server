package com.connectCo.domain.coupon.dto.request;

import com.connectCo.global.validation.annotation.ExistStore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponCreateRequest {
    @NotBlank(message = "쿠폰 이름은 필수 입력값입니다.")
//    @ExistStore 추후 CouponValidation 처리 해야함.
    private String name;

    @NotBlank(message = "가게 ID는 필수 입력값입니다.")
    @ExistStore // 해당 어노테이션으로 가게의 존재 여부를 확인할 수 있습니다.
    private UUID storeId;

    @NotBlank(message = "쿠폰 만료 날짜는 필수 입력값입니다.")
    private String expiredAt; // ISO 8601 형식의 날짜와 시간 문자열을 받습니다.

    @NotBlank(message = "쿠폰 설명은 필수 입력값입니다.")
    private String description;


    private String priorityTarget;

    private String notification;
}
