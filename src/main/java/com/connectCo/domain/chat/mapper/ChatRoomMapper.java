package com.connectCo.domain.chat.mapper;

import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.dto.response.ChatRoomResponse;
import com.connectCo.domain.chat.dto.response.EnterChatRoomResponse;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.member.entity.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatRoomMapper {

    public ChatRoom toChatRoom(Profile sender, Profile receiver) {
        return ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    public ChatRoomResponse toChatRoomSummaryResponse(ChatRoom chatRoom, Long loginMemberId) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .otherProfileId(chatRoom.getSender().getId().equals(loginMemberId)
                        ? chatRoom.getReceiver().getId()
                        : chatRoom.getSender().getId())
                .otherProfileName(chatRoom.getSender().getId().equals(loginMemberId)
                        ? chatRoom.getReceiver().getName()
                        : chatRoom.getSender().getName())
                .recentMessage(chatRoom.getRecentMessage())
                .recentMessageTime(chatRoom.getRecentMessageTime())
                .build();
    }

    public EnterChatRoomResponse toEnterChatRoomResponse(Long chatRoomId, List<ChatResponse> chatList){
        return EnterChatRoomResponse.builder()
                .chatRoomId(chatRoomId)
                .chatList(chatList)
                .build();
    }
}
