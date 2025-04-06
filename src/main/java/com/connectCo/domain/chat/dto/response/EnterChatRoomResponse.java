package com.connectCo.domain.chat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EnterChatRoomResponse {
    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;
    private List<ChatResponse> chatList;
}
