package com.connectCo.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponUpdateRequest {
    @NotBlank(message = "쿠폰 이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "쿠폰 신청 마감일은 필수 입력값입니다.")
    private LocalDateTime expiredAt;

    @NotBlank(message = "쿠폰 세부 설명은 필수 입력값입니다.")
    private String description;

    private String priorityTarget;

    private String notification;

    @Schema(description = "기존 이미지 URL 목록", example = "[\"s3 url1\", \"s3 url2\"]")
    private List<String> existingImages;
}
