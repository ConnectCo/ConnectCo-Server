package com.connectCo.domain.Member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberFcmTokenResponse {
    private Long memberId;
    private String fcmToken;
}
