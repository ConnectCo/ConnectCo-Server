package com.connectCo.domain.Member.service;


import com.connectCo.config.jwt.JwtToken;
import com.connectCo.domain.Member.entity.Member;

public interface AuthService {
    JwtToken getToken(Member member);

    Long getLoginMemberId();

    Member getLoginMember();
}
