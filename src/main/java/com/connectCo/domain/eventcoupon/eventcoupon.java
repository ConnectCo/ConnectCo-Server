package com.connectCo.domain.eventcoupon;

import com.connectCo.domain.BaseEntity.baseEntity;
import com.connectCo.domain.coupon.coupon;
import com.connectCo.domain.event.event;
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
public class eventcoupon extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_coupon_id",nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "event_id",nullable = false)
    private event event;

    @ManyToOne
    @JoinColumn(name = "coupon_id",nullable = false)
    private coupon coupon;

    @Column(nullable = false)
    private int validCount;

    @Column(nullable = false)
    private Date validDate;

    @Column(nullable = false)
    private String accessUrl;

    @Column(nullable = false)
    private String authorizeCode;
}
