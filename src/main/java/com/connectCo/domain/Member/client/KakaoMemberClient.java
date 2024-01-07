package com.connectCo.domain.Member.client;

import com.connectCo.domain.Member.dto.client.KakaoMemberResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoMemberClient {

    private final WebClient webClient;

    public KakaoMemberClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .build();
    }

    public String getKakaoUserId(String accessToken) {
        KakaoMemberResponse response = webClient
                .get()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoMemberResponse.class)
                .block();
        if(response != null) {
            return response.getId();
        }
        throw new RuntimeException("올바르지 않은 엑세스 토큰입니다.");
    }
}
