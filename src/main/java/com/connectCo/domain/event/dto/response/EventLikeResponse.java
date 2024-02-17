package com.connectCo.domain.event.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class EventLikeResponse {
    private UUID eventId;
    private UUID memberId;
}
