package com.connectCo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum  ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON402", "금지된 요청입니다."),
    UNAUTHORIZED_MODIFY(HttpStatus.BAD_REQUEST, "COMMON403", "수정, 삭제 권한이 없습니다."),
    USER_NOT_ADMIN(HttpStatus.UNAUTHORIZED, "COMMON404", "관리자만 사용 가능한 API입니다."),
    UNKNOWN_INQUIRY_TYPE(HttpStatus.BAD_REQUEST, "COMMON405", "알 수 없는 조회 타입입니다."),

    // JWT Token
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT401", "잘못된 JWT 토큰입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT402", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT403", "지원하지 않는 JWT 토큰입니다."),
    EMPTY_JWT_CLAIMS(HttpStatus.UNAUTHORIZED, "JWT404", "JWT claims string is empty입니다."),
    UNAUTHORIZED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT405", "권한 정보가 없는 토큰입니다."),

    // Profile
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROFILE401", "프로필을 찾을 수 없습니다."),
    UNAUTHORIZED_PROFILE(HttpStatus.UNAUTHORIZED, "PROFILE402", "프로필 권한이 없습니다."),
    INVALID_PROFILE_TYPE(HttpStatus.BAD_REQUEST, "PROFILE403", "잘못된 프로필 타입입니다."),

    // MAP
    INVALID_LOCATION(HttpStatus.BAD_REQUEST, "MAP01", "잘못된 위치 정보입니다."),
    INVALID_RADIUS(HttpStatus.BAD_REQUEST, "MAP02", "반경은 양의 정수 값이어야 합니다."),

    // Member
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER401", "사용자를 찾을 수 없습니다."),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "MEMBER402", "이미 존재하는 사용자입니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "MEMBER403", "권한이 존재하지 않습니다."),

    // Store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE401", "가게를 찾을 수 없습니다."),
    STORE_NAME_DUPLICATION(HttpStatus.CONFLICT, "STORE402", "이미 존재하는 가게명입니다."),
    STORE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "STORE403", "가게 등록 제한을 초과하였습니다."),

    //Event
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "EVENT401", "이벤트를 찾을 수 업습니다."),

    // Organization
    ORGANIZATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "ORGANIZATION401", "조직을 찾을 수 없습니다."),
    ORGANIZATION_NAME_DUPLICATION(HttpStatus.BAD_REQUEST, "ORGANIZATION402", "이미 존재하는 조직명입니다."),
    ORGANIZATION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ORGANIZATION403", "조직 등록 제한을 초과하였씁니다."),

    //Coupon
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND,"COUPON401","해당 쿠폰을 찾을 수 없습니다."),

    //Chat
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND,"CHATROOM401","해당 채팅방을 찾을 수 없습니다."),

    //firebase
    FIREBASE_INIT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FIREBASE500", "Firebase 초기화에 실패했습니다."),
    FIREBASE_PUSH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FIREBASE501", "Firebase 알림 푸쉬에 실패했습니다.");
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
