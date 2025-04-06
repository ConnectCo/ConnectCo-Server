package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.dto.response.EnterChatRoomResponse;
import com.connectCo.domain.member.entity.Member;
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
import com.connectCo.global.validation.ParamValidator;
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
    public ChatRoomResponse createChatRoom(Long senderId, Long receiverId, ProfileType senderProfileType, ProfileType receiverProfileType) {
        //프로필 타입 유효성 검사
        ParamValidator.validChatProfileType(senderProfileType, receiverProfileType);

        Profile sender = profileRepository.getProfile(senderId, senderProfileType);
        Profile receiver = profileRepository.getProfile(receiverId, receiverProfileType);

        ChatRoom chatRoom = createAndSaveChatRoom(sender, receiver);

        return chatRoomMapper.toChatRoomSummaryResponse(chatRoom, senderId);
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

    @Override
    @Transactional
    public EnterChatRoomResponse enterChatRoom(Long loginProfileId, ProfileType loginProfileType,Long otherProfileId, ProfileType otherProfileType){
        //프로필 타입 유효성 검사
        ParamValidator.validChatProfileType(loginProfileType, otherProfileType);


        Profile sender = profileRepository.getProfile(loginProfileId, loginProfileType);
        Profile receiver = profileRepository.getProfile(otherProfileId, otherProfileType);

        ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiver(sender, receiver)
                .orElseGet(() -> createAndSaveChatRoom(sender, receiver));

        List<ChatResponse> chatResponses = chatRoom.getChats().stream()
                .map(chatMapper::toChatResponse)
                .collect(Collectors.toList());

        return chatRoomMapper.toEnterChatRoomResponse(chatRoom.getId(),chatResponses);
    }

    public ChatRoom createAndSaveChatRoom(Profile sender, Profile receiver) {
        ChatRoom chatRoom = chatRoomMapper.toChatRoom(sender, receiver);
        return chatRoomRepository.save(chatRoom);
    }
}
