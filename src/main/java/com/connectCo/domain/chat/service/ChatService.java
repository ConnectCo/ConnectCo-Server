package com.connectCo.domain.chat.service;

import com.connectCo.domain.chat.dto.request.CreateChatRequest;
import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.dto.response.CreateChatResponse;

import java.util.List;

public interface ChatService {
    CreateChatResponse createChat(CreateChatRequest request);
}
