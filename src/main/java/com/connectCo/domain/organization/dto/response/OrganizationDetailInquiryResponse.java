package com.connectCo.domain.organization.dto.response;

import com.connectCo.global.common.dto.AddressResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationDetailInquiryResponse {
    @Schema(description = "조직 ID", example = "1")
    private Long organizationId;

    @Schema(description = "조직 이름", example = "조직 A")
    private String name;

    @Schema(description = "조직 설명", example = "이것은 조직 A 입니다.")
    private String description;

    @Schema(description = "조직 주소")
    private AddressResponse address;

    @Schema(description = "조직 연락처", example = "123-456-7890")
    private String phoneNumber;

    @Schema(description = "조직 프로필 이미지", example = "url")
    private String profileImage;

    @Schema(description = "조직 쿠폰 목록(2개만)")
    List<OrganizationEvent> events;

    @Schema(description = "신청 쿠폰 개수")
    private Integer appliedCouponCount;

    @Schema(description = "좋아요 여부")
    Boolean isLike;

    @Schema(description = "내 조직 여부")
    Boolean isMine;

    @Getter
    @Builder
    public static class OrganizationEvent {
        @Schema(description = "이벤트 ID", example = "1")
        private Long eventId;

        @Schema(description = "이벤트 이름", example = "이벤트 A")
        private String name;

        @Schema(description = "이벤트 썸네일 이미지")
        private String eventThumbnail;

        @Schema(description = "협찬 제한 마감일", example = "2023-12-31")
        private LocalDate expiredAt;
    }
}
