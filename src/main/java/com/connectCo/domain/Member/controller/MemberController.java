package com.connectCo.domain.Member.controller;

import com.connectCo.domain.Member.dto.response.MemberLoginResponse;
import com.connectCo.domain.Member.service.MemberService;
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
    public MemberLoginResponse saveMemberByGoogle() {
        // TODO 구글 로그인 구현
        return null;
    }

    @PostMapping("/kakao")
    public MemberLoginResponse saveMemberByKakao() {
        // TODO 카카오 로그인 구현
        return null;
    }


    @PostMapping("/naver")
    public MemberLoginResponse saveMemberByNaver(@RequestParam(name = "accessToken") String accessToken) {
        return memberService.saveMemberByNaver(accessToken);
    }
}
