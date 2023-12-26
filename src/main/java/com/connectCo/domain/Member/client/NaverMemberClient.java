package com.connectCo.domain.Member.client;

import com.connectCo.domain.Member.dto.client.NaverMemberResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NaverMemberClient {
    private final WebClient webClient;

    public NaverMemberClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://openapi.naver.com/v1/nid/me")
                .build();
    }

    public String getNaverUserId(String accessToken) {
         NaverMemberResponse response = webClient
                 .get()
                 .header("Authorization", "Bearer " + accessToken)
                 .retrieve()
                 .bodyToMono(NaverMemberResponse.class)
                 .block();
         if(response != null) {
             return response.getResponse().getId();
         }
         throw new RuntimeException("올바르지 않은 액세스 토큰입니다.");
    }

}
