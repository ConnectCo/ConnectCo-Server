package com.connectCo.domain.Member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponse {
    private UUID memberId;
    private String accessToken;
    private String refreshToken;
}
