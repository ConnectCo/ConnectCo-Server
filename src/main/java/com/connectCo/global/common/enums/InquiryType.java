package com.connectCo.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryType {
    DISTANCE("거리순"),
    RECOMMEND("추천순"),
    RECENT("최근순"),
    ;

    private final String description;
}
