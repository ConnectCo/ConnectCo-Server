package com.connectCo.domain.Member.service;

import com.connectCo.domain.Member.dto.response.MemberLoginResponse;

public interface MemberService {
    MemberLoginResponse saveMemberByNaver(String accessToken);
    MemberLoginResponse saveMemberByKakao(String accessToken);
    MemberLoginResponse saveMemberByGoogle(String accessToken);

}
