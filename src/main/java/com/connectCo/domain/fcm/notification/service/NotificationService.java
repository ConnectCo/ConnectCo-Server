package com.connectCo.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendAlert(String userId, String message) {
        String destination = "/topic/alerts/" + userId;  // 사용자별 알림 채널
        messagingTemplate.convertAndSend(destination, message);
    }
}
