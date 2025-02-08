package com.connectCo.domain.member.mapper;

import com.connectCo.config.security.jwt.JwtToken;
import com.connectCo.domain.member.dto.response.AuthTokenResponse;
import com.connectCo.domain.member.dto.response.ProfileListResponse;
import com.connectCo.domain.member.entity.LoginType;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public Member toMember(String clientId, LoginType loginType) {
        return Member.builder()
            .clientId(clientId)
            .loginType(loginType)
            .role(Role.USER)
            .build();
    }

    public AuthTokenResponse toAuthTokenResponse(
        Long memberId, Long profileId, ProfileType profileType, JwtToken jwtToken) {
        return AuthTokenResponse.builder()
            .memberId(memberId)
            .profile(toProfileInfo(profileId, profileType))
            .accessToken(jwtToken.getAccessToken())
            .refreshToken(jwtToken.getRefreshToken())
            .build();
    }

    public ProfileListResponse.ProfileResponse toProfileResponse(Profile profile) {
        return ProfileListResponse.ProfileResponse.builder()
            .profileId(profile.getId())
            .profileType(profile.getProfileType())
            .profileName(profile.getName())
            .profileImageUrl(profile.getProfileImage())
            .build();
    }

    private AuthTokenResponse.ProfileInfo toProfileInfo(Long profileId, ProfileType profileType) {
        return new AuthTokenResponse.ProfileInfo(profileId, profileType);
    }

}
