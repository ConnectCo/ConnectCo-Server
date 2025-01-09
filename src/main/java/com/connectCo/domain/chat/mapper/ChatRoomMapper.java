package com.connectCo.domain.chat.mapper;

import com.connectCo.domain.Member.entity.Member;
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

}
