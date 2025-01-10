package com.connectCo.domain.chat.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.repository.MemberRepository;
import com.connectCo.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.chat.mapper.ChatRoomMapper;
import com.connectCo.domain.chat.repository.ChatRoomRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final MemberRepository memberRepository;
    
    /*
     * 채팅방 생성
     */
    @Override
    public ChatRoom createChatRoom(Long senderId, Long receiverId) {
        Member sender = memberRepository.findById(senderId).orElseThrow(()->new CustomApiException(ErrorCode.USER_NOT_FOUND));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(()->new CustomApiException(ErrorCode.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomMapper.toChatRoom(sender, receiver);

        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatRoomSummaryResponse> getChatRoomsByMember(Long memberId){
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByMember(memberId);

        return chatRooms.stream()
                .map(chatRoom -> chatRoomMapper.toChatRoomSummaryResponse(chatRoom, memberId))
                .toList();
    }


}
