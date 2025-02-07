package com.connectCo.domain.coupon.dto.request;

import com.connectCo.global.validation.annotation.ExistStore;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponCreateRequest {
    @NotBlank(message = "쿠폰 이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "쿠폰 신청 마감일은 필수 입력값입니다.")
    private LocalDate expiredAt;

    @NotBlank(message = "쿠폰 세부 설명은 필수 입력값입니다.")
    private String description;

    private String priorityTarget;

    private String notification;
}
