package com.connectCo.domain.chat.dto.request;

import com.connectCo.domain.member.entity.ProfileType;
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
    private ProfileType senderProfileType;
    private ProfileType receiverProfileType;
    private String message;
}
