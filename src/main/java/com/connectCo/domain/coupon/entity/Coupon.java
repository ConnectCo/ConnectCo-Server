package com.connectCo.domain.coupon.entity;

import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String priorityTarget;

    @Column(nullable = false)
    private String notification;

    @Column(nullable = false)
    private LocalDate expiredAt;

//    @Enumerated(EnumType.STRING)
//    private CouponType couponType;
//
//    private int validCount;
//    private int validPeriod;
//    private LocalDate validDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Store store;

    @OneToMany(mappedBy = "coupon")
    private List<CouponImage> images=new ArrayList<>();

    public void changeImages(List<CouponImage> couponImages) {
        // 기존 이미지가 있다면 삭제
        if(this.images != null) removeImages();

        // 새로운 이미지로 변경
        this.images = couponImages;
    }

    private void removeImages() {
        this.images.forEach(BaseEntity::delete);
    }

}

