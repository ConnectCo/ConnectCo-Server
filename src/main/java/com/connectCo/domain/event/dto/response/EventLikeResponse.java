package com.connectCo.domain.event.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class EventLikeResponse {
    private Long eventId;
    private Long memberId;
}
