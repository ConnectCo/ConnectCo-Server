package com.connectCo.domain.chat.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.repository.MemberRepository;
import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.mapper.ChatMapper;
import com.connectCo.domain.chat.repository.ChatRepository;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.domain.fcm.service.FcmService;
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
    private final MemberRepository memberRepository;
    private final FcmService fcmService;

    @Override
    @Transactional
    public ChatResponse createChat(CreateChatRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseGet(()->chatRoomService.createChatRoom(request.getSenderId(), request.getReceiverId()));

        Chat chat = chatMapper.toChat(request, chatRoom);
        chatRepository.save(chat);

        Member member = memberRepository.findById(request.getReceiverId()).orElseThrow(()->new CustomApiException(ErrorCode.USER_NOT_FOUND));

        fcmService.sendPushNotification(member.getFcmToken(), "새로운 메시지", request.getMessage());

        return chatMapper.toChatResponse(chat);
    }
}
