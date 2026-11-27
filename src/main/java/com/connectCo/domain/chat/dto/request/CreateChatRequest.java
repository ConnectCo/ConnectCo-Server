package com.connectCo.domain.chat.dto.request;

import com.connectCo.domain.member.entity.ProfileType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 메시지 본문만 사용한다. chatRoomId·senderId·receiverId 등은 기존 클라이언트 호환을 위해 남겨 두었고 서버는 읽지 않는다
// (채팅방은 전송 경로 /app/{roomId}, 보낸 사람은 STOMP 연결 시 인증한 사용자, 받는 사람은 채팅방에서 정한다).
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
