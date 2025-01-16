package com.connectCo.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class    MemberLoginResponse {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
}
