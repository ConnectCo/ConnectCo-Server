package com.connectCo.domain.member.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class GoogleMemberClient {

    public String getGoogleUserId(String idToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(idToken);
            return decodedJWT.getClaim("sub").asString(); // 사용자 고유 ID 반환
        } catch (Exception e) {
            throw new RuntimeException("Invalid Google ID Token", e);
        }
    }
}
