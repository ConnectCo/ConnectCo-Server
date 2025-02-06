package com.connectCo.config.security;

import java.util.Arrays;
import java.util.stream.Stream;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConstant {

    // 비로그인 허용 API
    public static final String[] PUBLIC_URLS = {
        "/auth/login", "/auth/refresh",
        "/stores/*/detail", "/coupons/*/detail",
        "/v3/**", "/swagger-ui/**",
        "/test/**",
        "/ws/**"
    };

    // 로그인 필요 API
    public static final String[] AUTHENTICATED_URLS = {
        "/auth/select-profile",
        "/stores",
        "/organizations"
    };

    // 가게 프로필 전용 API
    public static final String[] STORE_URLS = {
        "coupons", "coupons/*", "/coupons/mine"
    };

    // 조직 프로필 전용 API
    public static final String[] ORGANIZATION_URLS = {
        "/stores/*/like", "/stores/like",
        "/coupons/*/like", "/coupons/like"
    };

    // 관리자 전용 API
    public static final String[] ADMIN_URLS = {
        "/admin/**"
    };

    // 모든 경로를 포함한 배열 (필요할 경우 사용)
    public static final String[] ALL_URLS =
        Stream.of(PUBLIC_URLS, AUTHENTICATED_URLS, STORE_URLS, ORGANIZATION_URLS, ADMIN_URLS)
            .flatMap(Arrays::stream)
            .toArray(String[]::new);
}
