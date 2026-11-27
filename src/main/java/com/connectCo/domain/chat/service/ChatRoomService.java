package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.dto.response.ChatPageResponse;
import com.connectCo.domain.chat.dto.response.ChatRoomResponse;
import com.connectCo.domain.chat.dto.response.EnterChatRoomResponse;
import com.connectCo.domain.member.entity.ProfileType;

import java.util.List;

public interface ChatRoomService {

    ChatRoomResponse createChatRoom(
            Long senderId, Long receiverId,
            ProfileType senderProfileType,
            ProfileType receiverProfileType);

    EnterChatRoomResponse enterChatRoom(Long loginProfileId, ProfileType loginProfileType, Long otherProfileId, ProfileType otherProfileType, int size);

    List<ChatRoomResponse> getChatRoomsByMember(Long profileId);

    ChatPageResponse getChatsByChatRoom(Long loginProfileId, Long chatRoomId, Long cursor, int size);
}
