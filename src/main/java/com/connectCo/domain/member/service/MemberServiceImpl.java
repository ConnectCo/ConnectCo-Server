package com.connectCo.domain.member.service;

import com.connectCo.config.jwt.JwtToken;
import com.connectCo.domain.member.client.GoogleMemberClient;
import com.connectCo.domain.member.client.KakaoMemberClient;
import com.connectCo.domain.member.client.NaverMemberClient;
import com.connectCo.domain.member.dto.response.MemberInfoResponse;
import com.connectCo.domain.member.dto.response.MemberLoginResponse;
import com.connectCo.domain.member.entity.LoginType;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.mapper.MemberMapper;
import com.connectCo.domain.member.repository.MemberRepository;
import com.connectCo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final NaverMemberClient naverMemberClient;
    private final KakaoMemberClient kakaoMemberClient;
    private final GoogleMemberClient googleMemberClient;

    private final AuthService authService;
    private final StoreService storeService;


    @Override
    @Transactional
    public MemberLoginResponse saveMemberByNaver(final String accessToken) {
        String clientId = getNaverClientId(accessToken);
        Optional<Member> member = memberRepository.findByClientIdAndLoginType(clientId, LoginType.NAVER);
        if(member.isPresent()) {
            return getMemberLoginResponse(member.get());
        }
        return getNewMemberLoginResponse(clientId, LoginType.NAVER);
    }

    @Override
    @Transactional
    public MemberLoginResponse saveMemberByKakao(String accessToken) {
        String clientId = getKakaoClientId(accessToken);
        Optional<Member> member = memberRepository.findByClientIdAndLoginType(clientId, LoginType.KAKAO);
        if(member.isPresent()) {
            return getMemberLoginResponse(member.get());
        }
        return getNewMemberLoginResponse(clientId, LoginType.KAKAO);
    }

    @Override
    @Transactional
    public MemberLoginResponse saveMemberByGoogle(String accessToken) {
        String clientId = getGoogleClientId(accessToken);
        Optional<Member> member = memberRepository.findByClientIdAndLoginType(clientId, LoginType.GOOGLE);
        if(member.isPresent()) {
            return getMemberLoginResponse(member.get());
        }
        return getNewMemberLoginResponse(clientId, LoginType.GOOGLE);
    }

    @Override
    @Transactional
    public MemberInfoResponse getMemberInfo() {
        Member member =  authService.getLoginMember();

        List<MemberInfoResponse.MyStores> storeList = storeService.getStoresByMember(member).stream()
                .map(memberMapper::toMyStores)
                .toList();

        return memberMapper.toMemberInfoResponse(member, storeList);
    }

    private String getNaverClientId(final String accessToken) {
        return naverMemberClient.getNaverUserId(accessToken);
    }

    private String getKakaoClientId(final String accessToken) {
        return kakaoMemberClient.getKakaoUserId(accessToken);
    }

    private String getGoogleClientId(final String accessToken) {
        return googleMemberClient.getGoogleUserId(accessToken);
    }

    private MemberLoginResponse getMemberLoginResponse(final Member member) {
        // TODO RefreshToken으로 AccessToken만 재발급 받도록 구현
        JwtToken jwtToken = authService.getToken(member);
        return memberMapper.toMemberLoginResponse(member.getId(), jwtToken);
    }

    private MemberLoginResponse getNewMemberLoginResponse(final String clientId, final LoginType loginType) {
        Member member = memberRepository.save(memberMapper.toMember(clientId, loginType));
        JwtToken jwtToken = authService.getToken(member);
        return memberMapper.toMemberLoginResponse(member.getId(), jwtToken);
    }
}
