package com.connectCo.domain.sponsorship.entity;


import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Sponsorship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int maxCount;

    @Enumerated(EnumType.STRING)
    private Sponsor sponsor;

    @Enumerated(EnumType.STRING)
    private SponsorshipStatus sponsorship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Event event;
}
