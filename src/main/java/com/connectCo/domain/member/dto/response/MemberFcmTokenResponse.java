package com.connectCo.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberFcmTokenResponse {
    private Long memberId;
    private String fcmToken;
}
