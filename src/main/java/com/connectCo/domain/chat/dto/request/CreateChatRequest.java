package com.connectCo.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatRequest {
    private Long chatRoomId;
    private Long senderId;
    private Long receiverId;
    private String message;
}
