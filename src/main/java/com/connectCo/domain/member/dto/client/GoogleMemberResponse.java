package com.connectCo.domain.member.dto.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleMemberResponse {
    private String sub;
    private String aud;
    private String iss;
    private String email;
}
