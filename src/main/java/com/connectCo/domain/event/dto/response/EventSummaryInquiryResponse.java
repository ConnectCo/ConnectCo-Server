package com.connectCo.domain.event.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class EventSummaryInquiryResponse {
    private Long eventId;
    private String organizationName;
    private String name;
    private LocalDate startAt;
    private LocalDate endAt;
    private String thumbnail;

}
