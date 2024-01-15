package com.connectCo.domain.event.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class EventInquiryByMemberResponse {
    private UUID eventId;
    private String organizationName;
    private String name;
    private LocalDate startAt;
    private LocalDate endAt;
}
