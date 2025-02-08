package com.connectCo.domain.member.service;


import com.connectCo.domain.member.dto.response.AuthTokenResponse;
import com.connectCo.domain.member.dto.response.ProfileListResponse;
import com.connectCo.domain.member.entity.LoginType;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.ProfileType;

public interface AuthService {

    AuthTokenResponse login(String accessToken, LoginType provider);
    ProfileListResponse getProfiles(Member member);
    AuthTokenResponse selectProfile(Long profileId, ProfileType profileType, Member member);
    void deleteProfile(Long profileId, ProfileType profileType, Member member);
    void logout(Long profileId, ProfileType profileType);
    void withdraw(Member member);
    AuthTokenResponse refresh(String refreshToken);
}
