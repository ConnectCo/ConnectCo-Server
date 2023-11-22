package com.connectCo.domain.member;

import com.connectCo.domain.BaseEntity.baseEntity;
import com.connectCo.domain.enums.loginType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class member extends baseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable =false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private loginType loginType;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

}