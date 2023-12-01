package com.connectCo.domain.eventcoupon;

import com.connectCo.domain.baseentity.BaseEntity;
import com.connectCo.domain.coupon.Coupon;
import com.connectCo.domain.event.Event;
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
public class EventCoupon extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id",nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id",nullable = false)
    private Coupon coupon;

    @Column(nullable = false)
    private int validCount;

    @Column(nullable = false)
    private LocalDate validDate;

    @Column(nullable = false)
    private String accessUrl;

    @Column(nullable = false)
    private String authorizeCode;

    @Builder
    public EventCoupon(UUID id, Event event, Coupon coupon, int validCount, LocalDate validDate, String accessUrl, String authorizeCode) {
        this.id = id;
        this.event = event;
        this.coupon = coupon;
        this.validCount = validCount;
        this.validDate = validDate;
        this.accessUrl = accessUrl;
        this.authorizeCode = authorizeCode;
    }
}
