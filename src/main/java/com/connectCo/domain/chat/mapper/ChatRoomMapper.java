package com.connectCo.domain.chat.mapper;

import com.connectCo.domain.chat.entity.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {

    public ChatRoom toChatRoom() {
        return ChatRoom.builder()
                .build();
    }

}
