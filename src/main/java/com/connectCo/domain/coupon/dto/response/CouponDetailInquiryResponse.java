package com.connectCo.domain.coupon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CouponDetailInquiryResponse {
    @Schema(description = "쿠폰 ID", example = "1")
    private Long id;

    @Schema(description = "가게 정보")
    private StoreInfo store;

    @Schema(description = "쿠폰 이름", example = "할인 쿠폰")
    private String name;

    @Schema(description = "쿠폰 설명", example = "이것은 할인 쿠폰입니다.")
    private String description;

    @Schema(description = "우선 협찬 대상")
    private String priorityTarget;

    @Schema(description = "유의사항")
    private String notification;

    @Schema(description = "신청 마감일", example = "2023-12-31")
    private LocalDate expiredAt;

    @Schema(description = "쿠폰 등록일", example = "2023-12-31")
    private LocalDate createdAt;

    @Schema(description = "이미지 URL 목록", example = "[\"url1\", \"url2\"]")
    private List<String> images;

    @Schema(description = "좋아요 여부")
    private Boolean isLike;

    @Schema(description = "내 쿠폰 여부")
    private Boolean isMine;

    @Schema(description = "협찬 이벤트 개수")
    private Integer eventCount;

    @Getter
    @AllArgsConstructor
    public static class StoreInfo {
        private Long storeId;
        private String name;
    }
}
