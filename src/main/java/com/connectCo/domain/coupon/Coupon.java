package com.connectCo.domain.coupon;

import com.connectCo.domain.baseentity.BaseEntity;
import com.connectCo.domain.enums.CouponType;
import com.connectCo.domain.store.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Where(clause = "deleted_at is null")
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "coupon_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

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
    private LocalDate validDate;

    @Builder
    public Coupon(UUID id, Store store, String title, String content, CouponType couponType, int validCount, int validPeriod, LocalDate validDate) {
        this.id = id;
        this.store = store;
        this.title = title;
        this.content = content;
        this.couponType = couponType;
        this.validCount = validCount;
        this.validPeriod = validPeriod;
        this.validDate = validDate;
    }
}

