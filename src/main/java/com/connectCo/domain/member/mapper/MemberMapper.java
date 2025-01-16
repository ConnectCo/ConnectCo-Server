package com.connectCo.domain.member.mapper;

import com.connectCo.config.jwt.JwtToken;
import com.connectCo.domain.member.dto.response.MemberInfoResponse;
import com.connectCo.domain.member.dto.response.MemberLoginResponse;
import com.connectCo.domain.member.entity.LoginType;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.Role;
import com.connectCo.domain.store.entity.Store;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberMapper {
    public Member toMember(String clientId, LoginType loginType) {
        return Member.builder()
                .clientId(clientId)
                .loginType(loginType)
                .role(Role.USER)
                .build();
    }

    public MemberLoginResponse toMemberLoginResponse(Long memberId, JwtToken jwtToken) {
        return MemberLoginResponse.builder()
                .memberId(memberId)
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }

    public MemberInfoResponse toMemberInfoResponse(Member member, List<MemberInfoResponse.MyStores> myStores) {
        return MemberInfoResponse.builder()
                .name(member.getName())
                .profileImage(member.getProfileImage())
                .myStores(myStores)
                .build();
    }

    public MemberInfoResponse.MyStores toMyStores(Store store) {
        return MemberInfoResponse.MyStores.builder()
                .storeId(store.getId())
                .name(store.getName())
                .build();
    }
}
