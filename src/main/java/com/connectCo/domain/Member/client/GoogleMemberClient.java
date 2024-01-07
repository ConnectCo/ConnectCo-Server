package com.connectCo.domain.Member.client;

import com.connectCo.domain.Member.dto.client.GoogleMemberResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GoogleMemberClient {
    private final WebClient webClient;

    public GoogleMemberClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://www.googleapis.com/oauth2/v3/userinfo")
                .build();
    }

    public String getGoogleUserId(String accessToken) {
        GoogleMemberResponse response = webClient
                .get()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(GoogleMemberResponse.class)
                .block();
        if(response != null) {
            return response.getSub();
        }
        throw new RuntimeException("올바르지 않은 액세스 토큰입니다.");
    }
}
