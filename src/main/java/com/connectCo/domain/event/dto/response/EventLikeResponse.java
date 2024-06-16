package com.connectCo.domain.event.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventLikeResponse {
    private Long eventId;
    private Long memberId;
}
