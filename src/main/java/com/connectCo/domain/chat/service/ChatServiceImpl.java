package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.event.ChatCreatedEvent;
import com.connectCo.domain.chat.mapper.ChatMapper;
import com.connectCo.domain.chat.repository.ChatRepository;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ChatResponse createChat(Long senderProfileId, Long chatRoomId, String message) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.CHATROOM_NOT_FOUND));

        // 보낸 사람이 이 방의 참여자인지 확인하고, 받는 사람은 요청 값이 아니라 방에서 정한다.
        if (!chatRoom.isParticipant(senderProfileId)) {
            throw new CustomApiException(ErrorCode.CHATROOM_FORBIDDEN);
        }
        Long receiverId = chatRoom.getOther(senderProfileId).getId();

        Chat chat = chatRepository.save(chatMapper.toChat(senderProfileId, message, chatRoom));
        chatRoom.updateRecentMessage(chat.getMessage(), chat.getCreatedAt());

        eventPublisher.publishEvent(new ChatCreatedEvent(chat.getId(), receiverId, chat.getMessage()));
        return chatMapper.toChatResponse(chat);
    }
}
