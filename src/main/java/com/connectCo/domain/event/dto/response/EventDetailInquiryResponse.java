package com.connectCo.domain.event.dto.response;

import com.connectCo.global.common.dto.AddressResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class EventDetailInquiryResponse {
    @Schema(description = "이벤트 ID", example = "1")
    private Long eventId;

    @Schema(description = "조직 정보")
    private OrganizationInfo organization;

    @Schema(description = "이벤트 이름", example = "할인 이벤트")
    private String name;

    @Schema(description = "이벤트 설명", example = "이벤트 설명입니다.")
    private String description;

    @Schema(description = "이벤트 시작일", example = "2023-12-31")
    private LocalDate startAt;

    @Schema(description = "이벤트 종료일", example = "2023-12-31")
    private LocalDate endAt;

    @Schema(description = "협찬 신청 마감일", example = "2023-12-31")
    private LocalDate expiredAt;

    @Schema(description = "혜택 대상")
    private String benefitTarget;

    @Schema(description = "우선 협찬 대상")
    private String priorityTarget;

    @Schema(description = "주소 정보")
    private AddressResponse address;

    @Schema(description = "유의사항")
    private String notification;

    @Schema(description = "이미지 URL 목록", example = "[\"url1\", \"url2\"]")
    private List<String> images;

    @Schema(description = "좋아요 여부")
    private Boolean isLike;

    @Schema(description = "내 이벤트 여부")
    private Boolean isMine;

    @Schema(description = "협찬 쿠폰 개수")
    private Integer couponCount;

    @Getter
    @AllArgsConstructor
    public static class OrganizationInfo {
        private Long organizationId;
        private String name;
    }
}
