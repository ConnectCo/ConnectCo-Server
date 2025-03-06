package com.connectCo.domain.chat.mapper;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.member.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {

    public ChatRoom toChatRoom(Profile sender, Profile receiver) {
        return ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    public ChatRoomSummaryResponse toChatRoomSummaryResponse(ChatRoom chatRoom, Long loginMemberId) {
        return ChatRoomSummaryResponse.builder()
                .chatRoomId(chatRoom.getId())
                .otherMemberId(chatRoom.getSender().getId().equals(loginMemberId)
                        ? chatRoom.getReceiver().getId()
                        : chatRoom.getSender().getId())
                .otherMemberName(chatRoom.getSender().getId().equals(loginMemberId)
                        ? chatRoom.getReceiver().getClientId()
                        : chatRoom.getSender().getClientId())
                .recentMessage(chatRoom.getRecentMessage())
                .recentMessageTime(chatRoom.getRecentMessageTime())
                .build();
    }

}
