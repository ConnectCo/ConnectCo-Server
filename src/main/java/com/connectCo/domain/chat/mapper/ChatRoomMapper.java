package com.connectCo.domain.chat.mapper;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.connectCo.domain.chat.entity.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {

    public ChatRoom toChatRoom(Member sender, Member receiver) {
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
                        ? chatRoom.getReceiver().getName()
                        : chatRoom.getSender().getName())
                .lastMessage("최근 메시지 구현 필요")//Todo 최근 메시지 반환 추가
                .build();
    }

}
