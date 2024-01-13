package com.connectCo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON402", "금지된 요청입니다."),

    // Member
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER401", "사용자를 찾을 수 없습니다."),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "MEMBER402", "이미 존재하는 사용자입니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "MEMBER403", "권한이 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
