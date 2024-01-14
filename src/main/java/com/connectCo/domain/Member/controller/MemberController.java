package com.connectCo.domain.Member.controller;

import com.connectCo.domain.Member.dto.response.MemberInfoResponse;
import com.connectCo.domain.Member.dto.response.MemberLoginResponse;
import com.connectCo.domain.Member.service.MemberService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "멤버 API", description = "멤버 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "구글 로그인 API")
    @PostMapping("/google")
    public BaseResponse<MemberLoginResponse> saveMemberByGoogle(@RequestParam(name = "accessToken") String accessToken) {
        return BaseResponse.onSuccess(memberService.saveMemberByGoogle(accessToken));
    }

    @Operation(summary = "카카오 로그인 API")
    @PostMapping("/kakao")
    public BaseResponse<MemberLoginResponse> saveMemberByKakao(@RequestParam(name = "accessToken") String accessToken) {
        return BaseResponse.onSuccess(memberService.saveMemberByKakao(accessToken));
    }

    @Operation(summary = "네이버 로그인 API")
    @PostMapping("/naver")
    public BaseResponse<MemberLoginResponse> saveMemberByNaver(@RequestParam(name = "accessToken") String accessToken) {
        return BaseResponse.onSuccess(memberService.saveMemberByNaver(accessToken));
    }


    @Operation(summary = "홈화면 유저 정보 조회 API")
    @GetMapping
    public BaseResponse<MemberInfoResponse> getMemberInfo() {
        return BaseResponse.onSuccess(memberService.getMemberInfo());
    }
}
