package com.connectCo.domain.Member.mapper;

import com.connectCo.domain.Member.dto.response.MemberLoginResponse;
import com.connectCo.domain.Member.entity.LoginType;
import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.entity.Role;
import com.connectCo.config.jwt.JwtToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MemberMapper {
    public Member toMember(String clientId, LoginType loginType) {
        return Member.builder()
                .clientId(clientId)
                .loginType(loginType)
                .role(new ArrayList<>(new ArrayList<>(List.of(Role.USER))))
                .build();
    }

    public MemberLoginResponse toMemberLoginResponse(UUID memberId, JwtToken jwtToken) {
        return MemberLoginResponse.builder()
                .memberId(memberId)
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
