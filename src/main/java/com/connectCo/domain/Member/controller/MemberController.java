package com.connectCo.domain.Member.controller;

import com.connectCo.domain.Member.dto.response.MemberLoginResponse;
import com.connectCo.domain.Member.service.MemberService;
import com.connectCo.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/google")
    public BaseResponse<MemberLoginResponse> saveMemberByGoogle(@RequestParam(name = "accessToken") String accessToken) {
        return BaseResponse.onSuccess(memberService.saveMemberByGoogle(accessToken));
    }

    @PostMapping("/kakao")
    public BaseResponse<MemberLoginResponse> saveMemberByKakao(@RequestParam(name = "accessToken") String accessToken) {
        return BaseResponse.onSuccess(memberService.saveMemberByKakao(accessToken));
    }


    @PostMapping("/naver")
    public BaseResponse<MemberLoginResponse> saveMemberByNaver(@RequestParam(name = "accessToken") String accessToken) {
        return BaseResponse.onSuccess(memberService.saveMemberByNaver(accessToken));
    }
}
