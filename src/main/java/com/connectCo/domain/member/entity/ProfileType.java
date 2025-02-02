package com.connectCo.domain.member.entity;

import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProfileType {

    STORE("가게"),
    ORGANIZATION("단체");

    private final String toKorean;

    public static ProfileType of(String toKorean) {
        if (toKorean.equals("STORE")) {
            return STORE;
        } else if (toKorean.equals("ORGANIZATION")) {
            return ORGANIZATION;
        } else {
            throw new CustomApiException(ErrorCode.INVALID_PROFILE_TYPE);
        }
    }
}
