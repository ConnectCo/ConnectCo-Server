package com.connectCo.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomResponse {
    private Long chatRoomId;
    private Long otherProfileId;
    private String otherProfileName;
    private String recentMessage;
    private LocalDateTime recentMessageTime;
}
