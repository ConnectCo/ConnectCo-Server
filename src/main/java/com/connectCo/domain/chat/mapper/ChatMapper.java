package com.connectCo.domain.chat.mapper;

import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public Chat toChat(CreateChatRequest request, ChatRoom chatRoom) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .senderId(request.getSenderId())
                .message(request.getMessage())
                .build();
    }

    public ChatResponse toChatResponse(Chat chat) {
        return ChatResponse.builder()
                .chatId(chat.getId())
                .senderId(chat.getSenderId())
                .message(chat.getMessage())
                .build();
    }
}
