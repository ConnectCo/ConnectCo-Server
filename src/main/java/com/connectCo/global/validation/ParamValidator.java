package com.connectCo.global.validation;

import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;

public class ParamValidator {

    // 위도, 경도 값 유효성 검사
    public static void validLocation(double latitude, double longitude) {
        if (!isValidLatitude(latitude) || !isValidLongitude(longitude)) {
            throw new CustomApiException(ErrorCode.INVALID_LOCATION);
        }
    }

    // 위도 값 범위 검사 (-90 ~ 90)
    private static boolean isValidLatitude(double latitude) {
        return latitude >= -90.0 && latitude <= 90.0;
    }

    // 경도 값 범위 검사 (-180 ~ 180)
    private static boolean isValidLongitude(double longitude) {
        return longitude >= -180.0 && longitude <= 180.0;
    }

    // 반경 값 유효성 검사
    public static void validRadius(int radius) {
        if (radius <= 0)
            throw new CustomApiException(ErrorCode.INVALID_RADIUS);
    }

    // 수정, 삭제 유효성 검사
    public static void validModify(Long memberId1, Long memberId2) {
        if (!memberId1.equals(memberId2))
            throw new CustomApiException(ErrorCode.UNAUTHORIZED_MODIFY);
    }
    
    //채팅 가게-조직 프로필 타입 유효성 검사
    public static void validChatProfileType(ProfileType senderProfileType, ProfileType receiverProfileType) {
        if ((senderProfileType == ProfileType.ORGANIZATION && receiverProfileType == ProfileType.ORGANIZATION) ||
                (senderProfileType == ProfileType.STORE && receiverProfileType == ProfileType.STORE))
            throw new CustomApiException(ErrorCode.INVALID_PROFILE_TYPE);
    }

}
