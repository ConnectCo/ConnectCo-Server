package com.connectCo.domain.chat.controller;

import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.connectCo.domain.chat.service.ChatRoomService;
import com.connectCo.domain.chat.service.ChatService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    public BaseResponse<List<ChatRoomSummaryResponse>> getChatRoomsByMember(@RequestParam Long memberId) {
        return BaseResponse.onSuccess(chatRoomService.getChatRoomsByMember(memberId));
    }

    @Operation(summary = "채팅방 메시지 조회 API")
    @GetMapping("/room/{chatRoomId}/chats")
    public BaseResponse<List<ChatResponse>> getChatByChatRoom(@PathVariable Long chatRoomId) {
        return BaseResponse.onSuccess(chatRoomService.getChatsByChatRoom(chatRoomId));
    }
}
