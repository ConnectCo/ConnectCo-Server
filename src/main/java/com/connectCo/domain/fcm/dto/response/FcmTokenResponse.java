package com.connectCo.domain.fcm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmTokenResponse {
    private Long profileId;
    private String fcmToken;
}
