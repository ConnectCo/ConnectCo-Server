package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.ChatResponse;

public interface ChatService {
    ChatResponse createChat(CreateChatRequest request);
}
