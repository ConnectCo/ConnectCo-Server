package com.connectCo.domain.chat.service;

import com.connectCo.domain.Member.repository.MemberRepository;
import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.dto.response.CreateChatResponse;
import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.mapper.ChatMapper;
import com.connectCo.domain.chat.mapper.ChatRoomMapper;
import com.connectCo.domain.chat.repository.ChatRepository;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomService chatRoomService;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ChatMapper chatMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;

    @Override
    @Transactional
    public CreateChatResponse createChat(CreateChatRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseGet(()->chatRoomService.createChatRoom(request.getSenderId(), request.getReceiverId()));

        Chat chat = chatMapper.toChat(request, chatRoom);
        return chatMapper.toCreateChatResponse(chat);
    }

    @Override
    public List<ChatResponse> getChatsByChatRoom(Long chatRoomId){
        return chatRepository.findChatsByChatRoomId(chatRoomId);
    }
}
