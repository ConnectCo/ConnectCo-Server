package com.connectCo.domain.store.entity;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
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
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String storeNumber;

    @Column(nullable = false)
    private String operatingTime;

    @Column(nullable = false)
    private int couponCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @OneToMany(mappedBy = "store")
    private List<StoreImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Coupon> coupons = new ArrayList<>();

    public void setAddress(Address address) {
        this.address = address;
    }

    public void changeImages(List<StoreImage> storeImages) {
        // 기존 이미지가 있다면 삭제
        if(this.images != null) removeImages();

        // 새로운 이미지로 변경
        this.images = storeImages;
    }

    private void removeImages() {
        this.images.forEach(BaseEntity::delete);
    }

    private void updateCoupon(List<Coupon> coupons) {
        this.coupons = coupons;
    }
}
