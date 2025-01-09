package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.mapper.ChatRoomMapper;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    
    /*
     * 채팅방 생성
     */
    @Override
    public ChatRoom createChatRoom() {
        ChatRoom chatRoom = chatRoomMapper.toChatRoom();

        return chatRoomRepository.save(chatRoom);

    }

}
