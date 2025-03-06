package com.connectCo.domain.chat.service;

import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.dto.response.ChatRoomResponse;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.mapper.ChatMapper;
import com.connectCo.domain.chat.mapper.ChatRoomMapper;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.domain.member.repository.ProfileRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final ProfileRepository profileRepository;
    private final ChatMapper chatMapper;

    /*
     * 채팅방 생성
     */
    @Override
    public ChatRoom createChatRoom(Long senderId, Long receiverId, ProfileType senderProfileType, ProfileType receiverProfileType) {

        Profile sender = profileRepository.getProfile(senderId, senderProfileType);
        Profile receiver = profileRepository.getProfile(receiverId, receiverProfileType);

        ChatRoom chatRoom = chatRoomMapper.toChatRoom(sender, receiver);

        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatRoomResponse> getChatRoomsByMember(Long profileId){
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByProfile(profileId);

        return chatRooms.stream()
                .map(chatRoom -> chatRoomMapper.toChatRoomSummaryResponse(chatRoom, profileId))
                .toList();
    }

    @Override
    @Transactional
    public List<ChatResponse> getChatsByChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.CHATROOM_NOT_FOUND));

        return chatRoom.getChats().stream()
                .map(chatMapper::toChatResponse)
                .collect(Collectors.toList());
    }
}
