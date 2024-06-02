package com.connectCo.domain.event.entity;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class EventCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accessUrl;

    @Column(nullable = false)
    private String authorizeCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Coupon coupon;

//    private int validCount;
//    private LocalDate validDate;
}
