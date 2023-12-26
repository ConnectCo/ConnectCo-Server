package com.connectCo.domain.Member.service;

import com.connectCo.domain.Member.entity.Member;
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

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public JwtToken getToken(Member member) {
        JwtToken jwtToken = jwtTokenProvider.generateToken(member.getClientId());
        member.saveRefreshToken(jwtToken.getRefreshToken());
        return jwtToken;
    }
}
