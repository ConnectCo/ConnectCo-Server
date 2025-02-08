package com.connectCo.domain.member.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 15 * 24 * 60 * 60 * 1000; // 15일

    /**
     * Refresh Token 저장
     */
    public void saveRefreshToken(Long profileId, String profileType, String refreshToken) {
        String redisKey = generateKey(profileId, profileType);
        redisTemplate.opsForValue().set(
            redisKey,
            refreshToken,
            REFRESH_TOKEN_EXPIRE_TIME,
            TimeUnit.MILLISECONDS
        );
    }

    /**
     * Refresh Token 조회
     */
    public String getRefreshToken(Long profileId, String profileType) {
        return redisTemplate.opsForValue().get(
            generateKey(profileId, profileType)
        );
    }

    /**
     * Refresh Token 삭제
     */
    public void deleteRefreshToken(Long profileId, String profileType) {
        redisTemplate.delete(generateKey(profileId, profileType));
    }

    /**
     * Redis 키 생성
     */
    private String generateKey(Long profileId, String profileType) {
        return "refreshToken:profile:" + profileId + ":" + profileType;
    }
}
