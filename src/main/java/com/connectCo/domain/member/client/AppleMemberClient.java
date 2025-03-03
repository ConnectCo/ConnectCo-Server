package com.connectCo.domain.member.client;

import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleMemberClient {

    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";
    @Value("${social.apple.client-id}")
    private String APPLE_CLIENT_ID;
    private static final long CACHE_TTL = 10 * 60 * 1000; // 10분

    private volatile JWKSet cachedJwkSet;
    private volatile long lastFetchedTime = 0;

    public String getAppleUserId(String identityToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(identityToken);
            String keyId = signedJWT.getHeader().getKeyID();

            // 애플 공개 키 가져오기 (캐싱 적용)
            JWKSet jwkSet = getAppleJwkSet();
            List<JWK> keys = jwkSet.getKeys();

            // Key ID에 맞는 공개 키 찾기
            JWK matchedKey = keys.stream()
                .filter(k -> Objects.equals(k.getKeyID(), keyId) && k.getKeyUse() == KeyUse.SIGNATURE)
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Apple public key not found for keyId: {}", keyId);
                    return new CustomApiException(ErrorCode.INVALID_APPLE_TOKEN);
                });

            // 공개 키로 JWT 검증
            PublicKey publicKey = matchedKey.toRSAKey().toPublicKey();
            if (!signedJWT.verify(new com.nimbusds.jose.crypto.RSASSAVerifier((RSAPublicKey) publicKey))) {
                log.warn("Apple identityToken signature verification failed");
                throw new CustomApiException(ErrorCode.INVALID_APPLE_TOKEN);
            }

            // 만료 시간(exp) 검증
            Instant expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            if (Instant.now().isAfter(expirationTime)) {
                log.warn("Apple identityToken is expired");
                throw new CustomApiException(ErrorCode.EXPIRED_APPLE_TOKEN);
            }

            // Audience(aud) 검증
            String audience = signedJWT.getJWTClaimsSet().getAudience().get(0);
            if (!APPLE_CLIENT_ID.equals(audience)) {
                log.warn("Invalid Apple identityToken audience: {}", audience);
                throw new CustomApiException(ErrorCode.INVALID_APPLE_TOKEN_AUDIENCE);
            }

            // 검증 후 'sub' 값 반환 (애플의 고유 사용자 ID)
            return signedJWT.getJWTClaimsSet().getSubject();

        } catch (ParseException | JOSEException | IOException e) {
            log.error("AppleMemberClient.getAppleUserId() error", e);
            throw new CustomApiException(ErrorCode.INVALID_APPLE_TOKEN);
        }
    }

    private JWKSet getAppleJwkSet() throws IOException, ParseException {
        long currentTime = System.currentTimeMillis();
        if (cachedJwkSet == null || currentTime - lastFetchedTime > CACHE_TTL) {
            synchronized (this) {
                if (cachedJwkSet == null || currentTime - lastFetchedTime > CACHE_TTL) {
                    cachedJwkSet = JWKSet.load(new URL(APPLE_PUBLIC_KEYS_URL));
                    lastFetchedTime = currentTime;
                    log.info("Fetched new Apple public keys from {}", APPLE_PUBLIC_KEYS_URL);
                }
            }
        }
        return cachedJwkSet;
    }
}
