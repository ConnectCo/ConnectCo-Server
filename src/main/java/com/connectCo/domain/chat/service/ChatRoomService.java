package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.connectCo.domain.chat.entity.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    ChatRoom createChatRoom(Long senderId, Long receiverId);

    List<ChatRoomSummaryResponse> getChatRoomsByMember(Long memberId);

    List<ChatResponse> getChatsByChatRoom(Long chatRoomId);
}
