package com.connectCo.domain.Member.mapper;

import com.connectCo.domain.Member.entity.LoginType;
import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.entity.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberMapper {
    public Member toMember(String email, LoginType loginType) {
        return Member.builder()
                .email(email)
                .loginType(loginType)
                .role(new ArrayList<>(new ArrayList<>(List.of(Role.USER))))
                .build();
    }
}
