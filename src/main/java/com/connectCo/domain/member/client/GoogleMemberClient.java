package com.connectCo.domain.member.client;

import com.connectCo.domain.member.dto.client.GoogleMemberResponse;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GoogleMemberClient {

    private final WebClient webClient;

    public GoogleMemberClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getGoogleUserId(String idToken) {
        GoogleMemberResponse response = webClient.get()
            .uri("https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken)
            .retrieve()
            .bodyToMono(GoogleMemberResponse.class)
            .block();

        if (response != null) {
            return response.getSub(); // 사용자 고유 ID 반환
        }

        throw new CustomApiException(ErrorCode.INVALID_GOOGLE_TOKEN);
    }
}
