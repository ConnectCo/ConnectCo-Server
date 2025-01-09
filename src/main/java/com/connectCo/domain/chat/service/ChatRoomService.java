package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.entity.ChatRoom;

public interface ChatRoomService {
    ChatRoom createChatRoom(Long senderId, Long receiverId);
    //Todo 토큰 기반으로 변경
}
