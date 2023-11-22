package com.connectCo.domain.coupon;

import com.connectCo.domain.BaseEntity.baseEntity;
import com.connectCo.domain.enums.CouponType;
import com.connectCo.domain.store.store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class coupon extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "coupon_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private store Store;

    @Column( nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(nullable = false)
    private int validCount;

    @Column(nullable = false)
    private int validPeriod;

    @Column(nullable = false)
    private Date validDate;
}

