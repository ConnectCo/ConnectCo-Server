package com.connectCo.domain.Member.service;


import com.connectCo.domain.Member.entity.LoginType;
import com.connectCo.global.config.jwt.JwtToken;

public interface AuthService {
    JwtToken getToken(String email, LoginType loginType);
}
