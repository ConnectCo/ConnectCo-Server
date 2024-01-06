package com.connectCo.domain.Member.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMemberResponse {
    private String id;
    private Properties properties;

    @Getter
    public static class Properties {
        private String nickname;
    }
}
