package com.connectCo.domain.store.dto.response;

import com.connectCo.global.common.dto.AddressResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Builder
public class StoreDetailInquiryResponse {
    @Schema(description = "가게 ID", example = "1")
    private Long storeId;

    @Schema(description = "가게 이름", example = "가게 A")
    private String name;

    @Schema(description = "가게 설명", example = "이것은 가게 A 입니다.")
    private String description;

    @Schema(description = "가게 주소")
    private AddressResponse address;

    @Schema(description = "가게 연락처", example = "123-456-7890")
    private String phoneNumber;

    @Schema(description = "가게 운영 시간", example = "09:00 - 18:00")
    private String operatingTime;

    @Schema(description = "이미지 URL 목록", example = "[\"url1\", \"url2\"]")
    private List<String> images;

    @Schema(description = "가게 쿠폰 목록(2개만)")
    private List<StoreCoupon> coupons;

    @Schema(description = "신청 이벤트 개수")
    private Integer appliedEventCount;

    @Schema(description = "좋아요 여부")
    Boolean isLike;

    @Schema(description = "내 가게 여부")
    Boolean isMine;

    @Getter
    @Builder
    public static class StoreCoupon {
        @Schema(description = "쿠폰 ID", example = "1")
        private Long couponId;

        @Schema(description = "쿠폰 이름", example = "할인 쿠폰")
        private String name;

        @Schema(description = "쿠폰 썸네일 이미지")
        private String couponThumbnail;

        @Schema(description = "신청 마감일", example = "2023-12-31")
        private LocalDate expiredAt;
    }
}
