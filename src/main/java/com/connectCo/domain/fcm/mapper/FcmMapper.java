package com.connectCo.domain.fcm.mapper;

import org.springframework.stereotype.Component;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Component
public class FcmMapper {

    public Message ToMessage(String targetToken, String title, String body){
        return Message.builder()
                .setToken(targetToken)
                .setNotification(ToNotification(title, body))
                .build();
    }

    public Notification ToNotification(String title, String body){
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }
}
