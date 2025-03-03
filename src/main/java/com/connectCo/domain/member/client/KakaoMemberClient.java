package com.connectCo.domain.member.client;

import com.connectCo.domain.member.dto.client.KakaoMemberResponse;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
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
        throw new CustomApiException(ErrorCode.INVALID_KAKAO_TOKEN);
    }
}
