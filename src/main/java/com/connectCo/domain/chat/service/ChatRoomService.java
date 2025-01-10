package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.connectCo.domain.chat.entity.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    ChatRoom createChatRoom(Long senderId, Long receiverId);
    //Todo 토큰 기반으로 변경

    List<ChatRoomSummaryResponse> getChatRoomsByMember(Long memberId);
}
