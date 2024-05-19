package com.connectCo.domain.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class EventIdResponse {
    private UUID eventId;
}
