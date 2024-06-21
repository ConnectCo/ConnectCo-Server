package com.connectCo.domain.sponsorship.entity;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.event.entity.Event;
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
public class Sponsorship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isComplete = false;

    @Enumerated(EnumType.STRING)
    private Sponsor sponsor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Coupon coupon;
}
