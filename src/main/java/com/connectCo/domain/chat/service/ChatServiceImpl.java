package com.connectCo.domain.chat.service;

import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.repository.MemberRepository;
import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.mapper.ChatMapper;
import com.connectCo.domain.chat.repository.ChatRepository;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.domain.fcm.service.FcmService;
import com.connectCo.domain.member.repository.ProfileRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomService chatRoomService;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ProfileRepository profileRepository;
    private final FcmService fcmService;

    @Override
    @Transactional
    public ChatResponse createChat(CreateChatRequest request) {
        ChatRoom chatRoom = findOrCreateChatRoom(request);
        Chat chat = saveChat(request, chatRoom);
        updateChatRoomRecentMessage(chatRoom, chat);
        sendPushNotificationToReceiver(request);

        return chatMapper.toChatResponse(chat);
    }

    private ChatRoom findOrCreateChatRoom(CreateChatRequest request) {
        return chatRoomRepository.findById(request.getChatRoomId())
                .orElseGet(() -> chatRoomService.createChatRoom(
                        request.getSenderId(), request.getReceiverId(),
                        request.getSenderProfileType(), request.getReceiverProfileType()
                ));
    }

    private Chat saveChat(CreateChatRequest request, ChatRoom chatRoom) {
        Chat chat = chatMapper.toChat(request, chatRoom);
        return chatRepository.save(chat);
    }

    private void updateChatRoomRecentMessage(ChatRoom chatRoom, Chat chat) {
        chatRoom.updateRecentMessage(chat.getMessage(), chat.getCreatedAt());
        chatRoomRepository.save(chatRoom);
    }

    private void sendPushNotificationToReceiver(CreateChatRequest request) {
        Profile profile = profileRepository.findById(request.getReceiverId()).orElseThrow(()->new CustomApiException(ErrorCode.USER_NOT_FOUND));
        fcmService.sendPushNotification(profile.getFcmToken(), "새로운 메시지", request.getMessage());
    }
}
