package com.connectCo.domain.member.entity;

import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}