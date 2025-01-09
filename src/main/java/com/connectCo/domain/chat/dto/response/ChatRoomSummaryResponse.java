package com.connectCo.domain.chat.dto.response;

import java.time.LocalDateTime;

public class ChatRoomSummaryResponse {
    private Long chatRoomId;
    private String lastMessage;
    LocalDateTime lastMessageTime;
}
