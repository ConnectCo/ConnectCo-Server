package com.connectCo.domain.Member.service;


import com.connectCo.domain.Member.entity.Member;
import com.connectCo.global.config.jwt.JwtToken;

public interface AuthService {
    JwtToken getToken(Member member);
}
