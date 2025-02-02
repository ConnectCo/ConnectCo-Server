package com.connectCo.domain.member.service;

import com.connectCo.config.security.jwt.JwtToken;
import com.connectCo.config.security.jwt.JwtTokenProvider;
import com.connectCo.config.security.jwt.RefreshTokenInfo;
import com.connectCo.domain.member.client.GoogleMemberClient;
import com.connectCo.domain.member.client.KakaoMemberClient;
import com.connectCo.domain.member.client.NaverMemberClient;
import com.connectCo.domain.member.dto.response.AuthTokenResponse;
import com.connectCo.domain.member.dto.response.ProfileListResponse;
import com.connectCo.domain.member.entity.LoginType;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.mapper.AuthMapper;
import com.connectCo.domain.member.repository.MemberRepository;
import com.connectCo.domain.member.repository.ProfileRepository;
import com.connectCo.domain.member.repository.TokenRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{

    private final NaverMemberClient naverMemberClient;
    private final KakaoMemberClient kakaoMemberClient;
    private final GoogleMemberClient googleMemberClient;

    private final AuthMapper authMapper;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthTokenResponse login(String accessToken, LoginType provider) {
        String clientId = UUID.randomUUID().toString();
//        String clientId = getClientIdByProvider(accessToken, provider);
        Optional<Member> member = memberRepository.findByClientIdAndLoginType(clientId, provider);

        if (member.isPresent()) {
            return generateTokensForExistingMember(member.get());
        }
        return generateTokensForNewMember(clientId, provider);
    }

    @Override
    public ProfileListResponse getProfiles(Member member) {
        List<Profile> profiles = profileRepository.findByMember(member);
        return new ProfileListResponse(
            profiles.stream()
                .filter(profile -> profile.getProfileType() == ProfileType.STORE)
                .map(authMapper::toProfileResponse)
                .toList(),
            profiles.stream()
                .filter(profile -> profile.getProfileType() == ProfileType.ORGANIZATION)
                .map(authMapper::toProfileResponse)
                .toList()
        );
    }

    @Override
    @Transactional
    public AuthTokenResponse selectProfile(Long profileId, ProfileType profileType, Member member) {
        Profile profile = profileRepository.getProfile(profileId, profileType);

        // 본인 프로필이 아닌 경우
        if (!profile.getMember().getId().equals(member.getId())) {
            throw new CustomApiException(ErrorCode.UNAUTHORIZED_PROFILE);
        }

        return generateTokensForProfile(member, profile);
    }

    @Override
    @Transactional
    public void deleteProfile(Long profileId, ProfileType profileType, Member member) {
        Profile profile = profileRepository.getProfile(profileId, profileType);

        // 본인 프로필이 아닌 경우
        if (!profile.getMember().getId().equals(member.getId())) {
            throw new CustomApiException(ErrorCode.UNAUTHORIZED_PROFILE);
        }

        deleteProfile(profile);
    }

    @Override
    @Transactional
    public void logout(Long profileId, ProfileType profileType) {
        tokenRepository.deleteRefreshToken(profileId, profileType.name());
    }

    @Override
    @Transactional
    public void withdraw(Member member) {
        List<Profile> profiles = profileRepository.findByMember(member);
        profiles.forEach(this::deleteProfile);

        member.delete();
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public AuthTokenResponse refresh(String refreshToken) {
        RefreshTokenInfo refreshTokenInfo = jwtTokenProvider.validateAndExtractRefreshToken(refreshToken);

        Profile profile = profileRepository.getProfile(
            refreshTokenInfo.getProfileId(), refreshTokenInfo.getProfileType()
        );
        Member member = profile.getMember();
        return generateTokensForProfile(member, profile);
    }

    private String getClientIdByProvider(String accessToken, LoginType provider) {
        return switch (provider) {
            case NAVER -> naverMemberClient.getNaverUserId(accessToken);
            case KAKAO -> kakaoMemberClient.getKakaoUserId(accessToken);
            case GOOGLE -> googleMemberClient.getGoogleUserId(accessToken);
            default -> throw new IllegalArgumentException("지원하지 않는 LoginType입니다: " + provider);
        };
    }

    private AuthTokenResponse generateTokensForExistingMember(final Member member) {
        JwtToken jwtToken = jwtTokenProvider.generateTokens(
            member.getId(), null, null, member.getRole().name()
        );

        return authMapper.toAuthTokenResponse(member.getId(), null, null, jwtToken);
    }

    private AuthTokenResponse generateTokensForNewMember(final String clientId, final LoginType loginType) {
        Member newMember = memberRepository.save(authMapper.toMember(clientId, loginType));
        JwtToken jwtToken = jwtTokenProvider.generateTokens(
            newMember.getId(), null, null, newMember.getRole().name()
        );

        return authMapper.toAuthTokenResponse(newMember.getId(), null, null, jwtToken);
    }

    private AuthTokenResponse generateTokensForProfile(final Member member, Profile profile) {
        JwtToken jwtToken = jwtTokenProvider.generateTokens(
            member.getId(), profile.getId(), profile.getProfileType().name(), member.getRole().name()
        );

        return authMapper.toAuthTokenResponse(member.getId(), profile.getId(), profile.getProfileType(), jwtToken);
    }

    void deleteProfile(Profile profile) {
        tokenRepository.deleteRefreshToken(profile.getId(), profile.getProfileType().name());
        profile.delete();
        profileRepository.save(profile);

        // TODO: 프로필과 관련된 모든 데이터 삭제
    }
}
