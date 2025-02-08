package com.connectCo.domain.member.dto.response;

import com.connectCo.domain.member.entity.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthTokenResponse {
    private Long memberId;
    private ProfileInfo profile;
    private String accessToken;
    private String refreshToken;

    @Getter
    @AllArgsConstructor
    public static class ProfileInfo {
        private Long profileId;
        private ProfileType profileType;
    }
}
