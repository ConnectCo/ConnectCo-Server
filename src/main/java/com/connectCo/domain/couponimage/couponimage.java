package com.connectCo.domain.couponimage;

import com.connectCo.domain.coupon.coupon;
import com.connectCo.domain.image.image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class couponimage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "coupon_image_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private coupon Coupon;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private image Image;
}
