package com.connectCo.domain.event.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class EventSummaryInquiryResponse {
    @Schema(description = "이벤트 ID", example = "1")
    private Long id;

    @Schema(description = "조직 이름", example = "조직 A")
    private String name;

    @Schema(description = "이벤트 이름", example = "이벤트 A")
    private String title;

    @Schema(description = "이벤트 협찬 마감일", example = "2021-12-31")
    private LocalDate expiredAt;

    @Schema(description = "이벤트 썸네일 URL", example = "thumbnail-url")
    private String thumbnail;

    @Schema(description = "위도", example = "36.123")
    private Double latitude;

    @Schema(description = "경도", example = "127.111")
    private Double longitude;
}
