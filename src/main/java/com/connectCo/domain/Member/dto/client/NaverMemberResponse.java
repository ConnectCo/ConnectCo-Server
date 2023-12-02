package com.connectCo.domain.Member.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverMemberResponse {
    private String resultcode;
    private String message;
    private Response response;

    @Getter
    public static class Response {
        private String id;
        private String email;
        private String mobile;
        private String mobile_e164;
    }
}
