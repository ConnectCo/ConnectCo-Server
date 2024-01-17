package com.connectCo.domain.Member.service;


import com.connectCo.domain.Member.entity.Member;
import com.connectCo.config.jwt.JwtToken;

import java.util.UUID;

public interface AuthService {
    JwtToken getToken(Member member);

    UUID getLoginMemberId();
    Member getLoginMember();
}
