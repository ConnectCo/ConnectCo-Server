package com.connectCo.domain.member.entity;

import org.hibernate.annotations.BatchSize;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
@Inheritance(strategy = InheritanceType.JOINED)
// 목록에서 연관 엔티티를 건마다 조회하지 않고 IN으로 묶어 조회한다
@BatchSize(size = 100)
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    protected String name;

    protected String profileImage;

    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private ProfileType profileType;

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void saveFcmToken(String fcmToken) { this.fcmToken = fcmToken; }

}
