package com.connectCo.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateChatResponse {
    private Long chatId;
    private Long chatRoomId;
    private String message;
    private LocalDateTime createdAt;
}
