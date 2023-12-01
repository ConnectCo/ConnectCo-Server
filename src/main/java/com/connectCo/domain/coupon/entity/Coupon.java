package com.connectCo.domain.coupon.entity;

import com.connectCo.global.utils.BaseEntity;
import com.connectCo.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column( nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    private int validCount;
    private int validPeriod;
    private LocalDate validDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Store store;
}

