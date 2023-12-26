package com.connectCo.domain.enventlike.entity;

import com.connectCo.global.utils.BaseEntity;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.Member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at is null")
public class EventLike extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Coupon coupon;

    @Column(nullable = false)
    private boolean isChecked;
}
