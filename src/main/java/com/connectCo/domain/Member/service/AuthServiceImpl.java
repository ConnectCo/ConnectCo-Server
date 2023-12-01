package com.connectCo.domain.Member.service;

import com.connectCo.domain.Member.entity.LoginType;
import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.mapper.MemberMapper;
import com.connectCo.domain.Member.repository.MemberRepository;
import com.connectCo.global.config.jwt.JwtToken;
import com.connectCo.global.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public JwtToken getToken(String email, LoginType loginType) {
        Member member = findOrCreateMember(email, loginType);

        JwtToken jwtToken = jwtTokenProvider.generateToken(email);

        member.saveRefreshToken(jwtToken.getRefreshToken());

        return jwtToken;
    }

    private Member findOrCreateMember(String email, LoginType loginType) {
        return memberRepository.findByEmailAndLoginType(email, loginType)
                .orElseGet(() -> createAndSaveMember(email, loginType));
    }

    private Member createAndSaveMember(String email, LoginType loginType) {
        Member newMember = memberMapper.toMember(email, loginType);
        return memberRepository.save(newMember);
    }
}
