package com.connectCo.domain.event.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventUpdateReponse {
    Long BookMarkId;
    LocalDateTime modifiedAt;
}
