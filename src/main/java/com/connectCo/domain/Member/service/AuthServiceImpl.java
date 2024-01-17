package com.connectCo.domain.Member.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.config.jwt.JwtToken;
import com.connectCo.config.jwt.JwtTokenProvider;
import com.connectCo.domain.Member.repository.MemberRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public JwtToken getToken(Member member) {
        JwtToken jwtToken = jwtTokenProvider.generateToken(member.getId().toString());
        member.saveRefreshToken(jwtToken.getRefreshToken());
        return jwtToken;
    }

    @Override
    public UUID getLoginMemberId() {
        return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public Member getLoginMember() {
        return memberRepository.findById(getLoginMemberId())
                .orElseThrow(() -> new CustomApiException(ErrorCode.USER_NOT_FOUND));
    }
}
