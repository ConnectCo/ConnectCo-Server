package com.connectCo.domain.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomSummaryResponse {
    private Long chatRoomId;
    private Long otherMemberId;
    private String otherMemberName;
    private String lastMessage;
    //LocalDateTime lastMessageTime;
}
