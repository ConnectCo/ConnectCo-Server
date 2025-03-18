package com.connectCo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static univcert.UnivCert.certify;
import static univcert.UnivCert.certifyCode;
import static univcert.UnivCert.status;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnivCertClient {

    @Value("${univcert.api-key}")
    private String apiKey;

    /**
     * 대학 이메일 인증 요청 (인증코드 발송)
     * @param email 인증할 이메일
     * @param universityName 대학교 이름
     * @return 인증 요청 성공 여부
     */
    public boolean requestVerification(String email, String universityName) {
        try {
            // univ_check를 true로 설정하여 해당 대학 재학 여부까지 확인
            String response = certify(apiKey, email, universityName, true);
            log.info("University verification requested: {} - {}", email, response);
            
            // 응답에 "success":true 포함 여부로 성공 확인
            return response.contains("\"success\":true");
        } catch (IOException e) {
            log.error("Failed to request university verification", e);
            return false;
        }
    }
    
    /**
     * 대학 이메일 인증코드 확인
     * @param email 인증 요청한 이메일
     * @param universityName 대학교 이름
     * @param code 인증 코드
     * @return 인증 성공 여부
     */
    public boolean verifyCode(String email, String universityName, int code) {
        try {
            String response = certifyCode(apiKey, email, universityName, code);
            log.info("University verification code check: {} - {}", email, response);
            
            return response.contains("\"success\":true");
        } catch (IOException e) {
            log.error("Failed to verify university code", e);
            return false;
        }
    }
    
    /**
     * 인증 상태 확인
     * @param email 확인할 이메일
     * @return 인증 상태 결과
     */
    public VerificationResult checkVerificationStatus(String email) {
        try {
            String response = status(apiKey, email);
            log.info("University verification status check: {} - {}", email, response);
            
            boolean verified = response.contains("\"success\":true");
            
            LocalDateTime certifiedDate = null;
            if (verified && response.contains("\"certified_date\":")) {
                String dateString = response.split("\"certified_date\":")[1].split("\"")[1];
                // 날짜 포맷 예시: "2023-01-03T09:30:22"
                certifiedDate = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
            }
            
            return new VerificationResult(verified, certifiedDate);
        } catch (IOException e) {
            log.error("Failed to check verification status", e);
            return new VerificationResult(false, null);
        }
    }
    
    /**
     * 인증 결과를 담는 내부 클래스
     */
    public static class VerificationResult {
        private final boolean verified;
        private final LocalDateTime certifiedDate;
        
        public VerificationResult(boolean verified, LocalDateTime certifiedDate) {
            this.verified = verified;
            this.certifiedDate = certifiedDate;
        }
        
        public boolean isVerified() {
            return verified;
        }
        
        public LocalDateTime getCertifiedDate() {
            return certifiedDate;
        }
    }
} 