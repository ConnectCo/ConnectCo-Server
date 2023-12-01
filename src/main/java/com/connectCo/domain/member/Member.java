package com.connectCo.domain.member;

import com.connectCo.domain.baseentity.BaseEntity;
import com.connectCo.domain.enums.LoginType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Where(clause = "deleted_at is null")
public class Member extends BaseEntity
{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "member_id",columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable =false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(nullable = false)
    private String refreshToken;

    @Builder
    public Member(UUID id, String name, String phoneNumber, LoginType loginType, String refreshToken) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.loginType = loginType;
        this.refreshToken = refreshToken;
    }
}