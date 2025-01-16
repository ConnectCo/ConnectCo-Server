package com.connectCo.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProfileType {

    STORE("가게"),
    ORGANIZATION("단체");

    private final String toKorean;
}
