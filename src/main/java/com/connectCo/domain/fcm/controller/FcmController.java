package com.connectCo.domain.fcm.controller;

import com.connectCo.domain.fcm.dto.response.FcmTokenResponse;
import com.connectCo.domain.fcm.service.FcmService;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FCM API", description = "FCM 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmController {

    private final FcmService fcmService;

    @Operation(summary = "Fcm 토큰 저장 API")
    @PostMapping("/token")
    @Parameters(value = {
            @Parameter(name = "profileId", description = "저장할 프로필 아아디 입력"),
            @Parameter(name = "profileType", description = "프로필 타입 입력(STORE, ORGANIZATION)"),
            @Parameter(name = "fcmToken", description = "저장할 FCM 토큰 입력")
    })
    public BaseResponse<FcmTokenResponse> saveFcmToken(
            @RequestParam(name = "profileId") Long profileId,
            @RequestParam(name = "profileType") ProfileType profileType,
            @RequestParam(name = "fcmToken") String fcmToken
    ){
        return BaseResponse.onSuccess(fcmService.saveFcmToken(profileId, profileType, fcmToken));
    }
}
