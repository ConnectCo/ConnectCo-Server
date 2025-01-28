package com.connectCo.domain.fcm.service;

import com.connectCo.domain.fcm.mapper.FcmMapper;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@DependsOn("firebaseConfig")
public class FcmService {

    private final FcmMapper fcmMapper;

    public void sendPushNotification(String targetToken, String title, String body) {

        try {
            Message message = fcmMapper.ToMessage(targetToken, title, body);//fcm 메세지 객체 빌드
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new CustomApiException(ErrorCode.FIREBASE_PUSH_FAILED);
        }
    }

}
