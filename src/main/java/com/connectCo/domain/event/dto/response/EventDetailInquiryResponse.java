package com.connectCo.domain.event.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class EventDetailInquiryResponse {
    private Long eventId;
    private String organizationName;
    private String name;
    private LocalDate expiredAt;
    private String description;
    private LocalDate startAt;
    private LocalDate endAt;
    private String benefitTarget;
    private String priorityTarget;
    private String detailAddress;
    private String notification;
    List<String> images;
}
