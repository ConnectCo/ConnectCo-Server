package com.connectCo.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatResponse {
    private Long chatId;
    private Long senderId;
    private String message;
    private LocalDateTime createdAt;
}
