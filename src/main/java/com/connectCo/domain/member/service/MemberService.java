package com.connectCo.domain.member.service;

import com.connectCo.domain.member.dto.response.MemberInfoResponse;
import com.connectCo.domain.member.dto.response.MemberLoginResponse;

public interface MemberService {
    MemberLoginResponse saveMemberByNaver(String accessToken);
    MemberLoginResponse saveMemberByKakao(String accessToken);
    MemberLoginResponse saveMemberByGoogle(String accessToken);
    MemberInfoResponse getMemberInfo();

}
