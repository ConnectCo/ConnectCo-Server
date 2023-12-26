package com.connectCo.domain.couponimage.entity;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.image.entity.Image;
import com.connectCo.global.utils.BaseEntity;
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
public class CouponImage extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Image image;

}
