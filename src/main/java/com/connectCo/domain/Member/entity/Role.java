package com.connectCo.domain.Member.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자"), USER("유저");

    private final String toKorean;
}
