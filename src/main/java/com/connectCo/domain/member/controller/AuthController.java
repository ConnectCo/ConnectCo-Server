package com.connectCo.domain.member.controller;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.member.dto.response.AuthTokenResponse;
import com.connectCo.domain.member.dto.response.ProfileListResponse;
import com.connectCo.domain.member.entity.LoginType;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.service.AuthService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public BaseResponse<AuthTokenResponse> login(
        @RequestParam(name = "accessToken") String accessToken,
        @RequestParam(name = "provider") LoginType provider
    ) {
        return BaseResponse.onSuccess(authService.login(accessToken, provider));
    }

    @Operation(summary = "프로필 조회 API")
    @PostMapping("/get-profiles")
    public BaseResponse<ProfileListResponse> getProfiles(
        @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return BaseResponse.onSuccess(authService.getProfiles(principal.member()));
    }

    @Operation(summary = "프로필 선택 API")
    @PostMapping("/select-profile")
    public BaseResponse<AuthTokenResponse> selectProfile(
        @RequestParam Long profileId,
        @RequestParam ProfileType profileType,
        @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return BaseResponse.onSuccess(authService.selectProfile(profileId, profileType, principal.member()));
    }

    @Operation(summary = "프로필 삭제 API")
    @PostMapping("/delete-profile")
    public BaseResponse<Void> deleteProfile(
        @RequestParam Long profileId,
        @RequestParam ProfileType profileType,
        @AuthenticationPrincipal PrincipalDetails principal
        ) {
        authService.deleteProfile(profileId, profileType, principal.member());
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/logout")
    public BaseResponse<Void> logout(
        @AuthenticationPrincipal PrincipalDetails principal
    ) {
        authService.logout(principal.profileId(), principal.profileType());
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "회원탈퇴 API")
    @PostMapping("/withdraw")
    public BaseResponse<Void> withdraw(
        @AuthenticationPrincipal PrincipalDetails principal
    ) {
        authService.withdraw(principal.member());
        return BaseResponse.onSuccess(null);
    }

    @Operation(summary = "토큰 재발급 API")
    @PostMapping("/refresh")
    public BaseResponse<AuthTokenResponse> refresh(
        @RequestHeader("RefreshToken") String refreshToken
    ) {
        return BaseResponse.onSuccess(authService.refresh(refreshToken));
    }
}
