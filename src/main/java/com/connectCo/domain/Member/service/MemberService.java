package com.connectCo.domain.Member.service;

import com.connectCo.domain.Member.dto.response.MemberInfoResponse;
import com.connectCo.domain.Member.dto.response.MemberLoginResponse;

import java.util.UUID;

public interface MemberService {
    MemberLoginResponse saveMemberByNaver(String accessToken);
    MemberLoginResponse saveMemberByKakao(String accessToken);
    MemberLoginResponse saveMemberByGoogle(String accessToken);
    MemberInfoResponse getMemberInfo();

}
