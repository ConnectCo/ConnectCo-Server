package com.connectCo.domain.enventlike;

import com.connectCo.domain.baseentity.BaseEntity;
import com.connectCo.domain.coupon.Coupon;
import com.connectCo.domain.member.Member;
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
public class EventLike extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id",nullable = false)
    private Coupon coupon;

    @Column(nullable = false)
    private boolean isChecked;

    @Builder
    public EventLike(UUID id, Member member, Coupon coupon, boolean isChecked) {
        this.id = id;
        this.member = member;
        this.coupon = coupon;
        this.isChecked = isChecked;
    }
}
