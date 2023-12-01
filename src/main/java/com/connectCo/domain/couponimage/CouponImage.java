package com.connectCo.domain.couponimage;

import com.connectCo.domain.coupon.Coupon;
import com.connectCo.domain.image.Image;
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
public class CouponImage {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Builder
    public CouponImage(UUID id, com.connectCo.domain.coupon.Coupon coupon, com.connectCo.domain.image.Image image) {
        this.id = id;
        this.coupon = coupon;
        this.image = image;
    }
}
