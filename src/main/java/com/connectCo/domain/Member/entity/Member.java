package com.connectCo.domain.Member.entity;

import com.connectCo.global.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Member extends BaseEntity
{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    private String phoneNumber;

    @Column(nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String refreshToken;

    @JoinColumn
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> role;

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}