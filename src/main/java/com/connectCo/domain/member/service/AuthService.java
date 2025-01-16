package com.connectCo.domain.member.service;


import com.connectCo.config.jwt.JwtToken;
import com.connectCo.domain.member.entity.Member;

public interface AuthService {
    JwtToken getToken(Member member);

    Long getLoginMemberId();

    Member getLoginMember();
}
