package com.connectCo.domain.sponsorship.entity;

import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sponsor {
    STORE("가게"),
    ORGANIZATION("단체");

    private final String toKorean;

    public static Sponsor of(String toKorean) {
        if (toKorean.equals("STORE")) {
            return STORE;
        } else if (toKorean.equals("ORGANIZATION")) {
            return ORGANIZATION;
        } else {
            throw new CustomApiException(ErrorCode.INVALID_Sponsor);
        }
    }
}
