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
        throw new CustomApiException(ErrorCode.INVALID_GOOGLE_TOKEN);
    }
}
