package com.connectCo.domain.coupon.entity;


import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.request.CouponUpdateRequest;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class    Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Store store;

    @OneToMany(mappedBy = "coupon")
    @Builder.Default
    private List<CouponImage> images = new ArrayList<>();

    public void changeImages(List<CouponImage> couponImages) {
        // 새로운 이미지로 변경
        this.images = couponImages;
    }

    public void updateDetails(CouponUpdateRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.priorityTarget = request.getPriorityTarget();
        this.notification = request.getNotification();
        this.expiredAt = request.getExpiredAt();

    }

}
