package com.connectCo.domain.fcm.service;

import com.connectCo.domain.fcm.dto.response.FcmTokenResponse;
import com.connectCo.domain.fcm.mapper.FcmMapper;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.repository.ProfileRepository;
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
public class FcmService {

    private final FcmMapper fcmMapper;
    private final ProfileRepository profileRepository;

    @DependsOn("firebaseConfig")
    public void sendPushNotification(String targetToken, String title, String body) {

        try {
            Message message = fcmMapper.ToMessage(targetToken, title, body);//fcm 메세지 객체 빌드
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new CustomApiException(ErrorCode.FIREBASE_PUSH_FAILED);
        }
    }

    public FcmTokenResponse saveFcmToken(Long profileId, ProfileType profileType, String fcmToken){
        Profile profile = profileRepository.getProfile(profileId, profileType);
        profile.saveFcmToken(fcmToken);
        profileRepository.save(profile);

        return new FcmTokenResponse(profileId, fcmToken);
    }


}
