package com.connectCo.domain.chat.controller;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.dto.response.ChatRoomResponse;
import com.connectCo.domain.chat.service.ChatRoomService;
import com.connectCo.domain.chat.service.ChatService;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "채팅 API", description = "채팅 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅 메세지 보내기 API")
    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")
    public BaseResponse<ChatResponse> createChat(@DestinationVariable String roomId, CreateChatRequest request){
        return BaseResponse.onSuccess(chatService.createChat(request));
    }

    @Operation(summary = "내 채팅방 목록 조회 API")
    @GetMapping("/rooms")
    public BaseResponse<List<ChatRoomResponse>> getChatRoomsByMember(@AuthenticationPrincipal PrincipalDetails principal) {
        return BaseResponse.onSuccess(chatRoomService.getChatRoomsByMember(principal.profileId()));
    }

    @Operation(summary = "채팅방 메시지 조회 API")
    @GetMapping("/rooms/{chatRoomId}/chats")
    public BaseResponse<List<ChatResponse>> getChatByChatRoom(@PathVariable Long chatRoomId) {
        return BaseResponse.onSuccess(chatRoomService.getChatsByChatRoom(chatRoomId));
    }

    @Operation(summary = "채팅방 생성 API")
    @PostMapping("/rooms/{chatRoomId}")
    public BaseResponse<ChatRoomResponse> createChatRoom(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam ProfileType senderProfileType,
            @RequestParam ProfileType receiverProfileType
    ){
        return BaseResponse.onSuccess(chatRoomService.createChatRoom(senderId, receiverId, senderProfileType, receiverProfileType));
    }
}
