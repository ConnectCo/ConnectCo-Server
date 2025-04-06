package com.connectCo.domain.chat.dto.request;

import com.connectCo.domain.member.entity.ProfileType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatRequest {
    @Schema(description = "채팅방 Id", example = "1")
    private Long chatRoomId;

    @Schema(description = "보내는 사람 Id", example = "1")
    private Long senderId;

    @Schema(description = "받는 Id", example = "2")
    private Long receiverId;

    @Schema(description = "보내는 사람 프로필 타입", example = "ORGANIZATION or STORE")
    private ProfileType senderProfileType;

    @Schema(description = "받는 사람 프로필 타입", example = "STORE or ORGANIZATION")
    private ProfileType receiverProfileType;

    @Schema(description = "메시지", example = "안녕 광수야?")
    private String message;
}
