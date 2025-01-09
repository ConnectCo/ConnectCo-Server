package com.connectCo.domain.chat.mapper;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.CreateChatResponse;
import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public Chat toChat(CreateChatRequest request, ChatRoom chatRoom, Member sender, Member receiver) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .message(request.getMessage())
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    public CreateChatResponse toCreateChatResponse(Chat chat) {
        return CreateChatResponse.builder()
                .chatId(chat.getId())
                .chatRoomId(chat.getChatRoom().getId())
                .senderId(chat.getSender().getId())
                .senderName(chat.getSender().getName())
                .receiverId(chat.getReceiver().getId())
                .receiverName(chat.getReceiver().getName())
                .message(chat.getMessage())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
