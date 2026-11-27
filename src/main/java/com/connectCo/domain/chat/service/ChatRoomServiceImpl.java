package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.dto.response.ChatPageResponse;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.dto.response.ChatRoomResponse;
import com.connectCo.domain.chat.dto.response.EnterChatRoomResponse;
import com.connectCo.domain.chat.entity.Chat;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.mapper.ChatMapper;
import com.connectCo.domain.chat.mapper.ChatRoomMapper;
import com.connectCo.domain.chat.repository.ChatRepository;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.repository.ProfileRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.global.validation.ParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    public static final int MAX_PAGE_SIZE = 100;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final ProfileRepository profileRepository;
    private final ChatMapper chatMapper;

    /*
     * 채팅방 생성
     */
    @Override
    @Transactional
    public ChatRoomResponse createChatRoom(Long senderId, Long receiverId, ProfileType senderProfileType, ProfileType receiverProfileType) {
        //프로필 타입 유효성 검사
        ParamValidator.validChatProfileType(senderProfileType, receiverProfileType);

        Profile sender = profileRepository.getProfile(senderId, senderProfileType);
        Profile receiver = profileRepository.getProfile(receiverId, receiverProfileType);

        ChatRoom chatRoom = chatRoomRepository.findRoomBetween(sender, receiver)
                .orElseGet(() -> createAndSaveChatRoom(sender, receiver));

        return chatRoomMapper.toChatRoomSummaryResponse(chatRoom, senderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRoomsByMember(Long profileId) {
        return chatRoomRepository.findChatRoomsByProfile(profileId).stream()
                .map(chatRoom -> chatRoomMapper.toChatRoomSummaryResponse(chatRoom, profileId))
                .toList();
    }

    /*
     * 채팅방 메시지 조회. 기존에는 방의 메시지 전체를 한 번에 내려줬다.
     * 최신 메시지부터 size개씩, 커서(이전 응답의 nextCursor)보다 오래된 메시지를 읽는다.
     */
    @Override
    @Transactional(readOnly = true)
    public ChatPageResponse getChatsByChatRoom(Long loginProfileId, Long chatRoomId, Long cursor, int size) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.CHATROOM_NOT_FOUND));
        if (!chatRoom.isParticipant(loginProfileId)) {
            throw new CustomApiException(ErrorCode.CHATROOM_FORBIDDEN);
        }
        return readPage(chatRoomId, cursor, size);
    }

    @Override
    @Transactional
    public EnterChatRoomResponse enterChatRoom(Long loginProfileId, ProfileType loginProfileType, Long otherProfileId, ProfileType otherProfileType, int size) {
        //프로필 타입 유효성 검사
        ParamValidator.validChatProfileType(loginProfileType, otherProfileType);

        Profile sender = profileRepository.getProfile(loginProfileId, loginProfileType);
        Profile receiver = profileRepository.getProfile(otherProfileId, otherProfileType);

        // 상대가 먼저 만든 방도 찾는다(기존에는 한 방향만 찾아 같은 두 사람의 방이 두 개 생길 수 있었다).
        ChatRoom chatRoom = chatRoomRepository.findRoomBetween(sender, receiver)
                .orElseGet(() -> createAndSaveChatRoom(sender, receiver));

        ChatPageResponse page = readPage(chatRoom.getId(), null, size);
        return chatRoomMapper.toEnterChatRoomResponse(chatRoom.getId(), page);
    }

    private ChatPageResponse readPage(Long chatRoomId, Long cursor, int size) {
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new CustomApiException(ErrorCode.INVALID_CHAT_PAGE_SIZE);
        }
        PageRequest limit = PageRequest.of(0, size + 1);
        List<Chat> rows = cursor == null
                ? chatRepository.findLatest(chatRoomId, limit)
                : chatRepository.findBefore(chatRoomId, cursor, limit);

        boolean hasNext = rows.size() > size;
        List<Chat> page = new ArrayList<>(hasNext ? rows.subList(0, size) : rows);
        Long nextCursor = hasNext ? page.get(page.size() - 1).getId() : null;

        // 화면에는 오래된 메시지가 위에 오므로 응답은 오래된 순으로 돌려준다.
        Collections.reverse(page);
        List<ChatResponse> chatList = page.stream().map(chatMapper::toChatResponse).toList();

        return ChatPageResponse.builder()
                .chatList(chatList)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    public ChatRoom createAndSaveChatRoom(Profile sender, Profile receiver) {
        ChatRoom chatRoom = chatRoomMapper.toChatRoom(sender, receiver);
        return chatRoomRepository.save(chatRoom);
    }
}
