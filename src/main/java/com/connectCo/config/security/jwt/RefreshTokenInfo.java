package com.connectCo.config.security.jwt;

import com.connectCo.domain.member.entity.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenInfo {
    private Long profileId;
    private ProfileType profileType;
}
