package com.connectCo.domain.event.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class EventDetailInquiryResponse {
    private UUID eventId;
    private String organizationName;
    private String name;
    private String description;
    private String priorityTarget;
    private String benefitTarget;
    private String notification;
    private LocalDate expiredAt;
    private LocalDate startAt;
    private LocalDate endAt;
    private String thumbnail;
}
