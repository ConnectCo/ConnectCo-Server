package com.connectCo.domain.Member.service;

import com.connectCo.domain.Member.client.NaverMemberClient;
import com.connectCo.domain.Member.dto.client.NaverMemberResponse;
import com.connectCo.domain.Member.dto.response.MemberLoginResponse;
import com.connectCo.domain.Member.entity.LoginType;
import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.mapper.MemberMapper;
import com.connectCo.domain.Member.repository.MemberRepository;
import com.connectCo.global.config.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final AuthService authService;
    private final NaverMemberClient naverMemberClient;


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

    private String getNaverClientId(final String accessToken) {
        return naverMemberClient.getNaverUserId(accessToken);
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
