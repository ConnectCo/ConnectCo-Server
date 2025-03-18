package com.connectCo.domain.organization.entity;

public enum UniversityVerificationStatus {
    NOT_VERIFIED("미인증"),
    PENDING("인증 진행 중"),
    VERIFIED("인증 완료");
    
    private final String description;
    
    UniversityVerificationStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 