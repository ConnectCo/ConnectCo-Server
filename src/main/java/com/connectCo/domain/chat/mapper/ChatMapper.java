package com.connectCo.domain.chat.mapper;

import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.CreateChatResponse;
import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public Chat toChat(CreateChatRequest request, ChatRoom chatRoom) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .message(request.getMessage())
                .build();
    }

    public CreateChatResponse toCreateChatResponse(Chat chat) {
        return CreateChatResponse.builder()
                .chatId(chat.getId())
                .chatRoomId(chat.getChatRoom().getId())
                .message(chat.getMessage())
                .build();
    }
}
