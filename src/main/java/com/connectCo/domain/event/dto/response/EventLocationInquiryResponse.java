package com.connectCo.domain.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventLocationInquiryResponse {
    private Long eventId;
    private String organizationName;
    private String name;
    private LocalDate startAt;
    private LocalDate endAt;
    private String thumbnail;
    private double latitude;
    private double longitude;
    private double distance;
}
