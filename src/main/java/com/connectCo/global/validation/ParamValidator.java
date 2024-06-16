package com.connectCo.global.validation;

import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;

public class ParamValidator {

    // 위도, 경도 값 유효성 검사
    public static void validLocation(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180)
            throw new CustomApiException(ErrorCode.INVALID_LOCATION);
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
}
