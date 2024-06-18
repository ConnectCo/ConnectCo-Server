package com.connectCo.domain.store.entity;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.dto.request.StoreUpdateRequest;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @OneToMany(mappedBy = "store")
    private List<StoreImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Coupon> coupons = new ArrayList<>();

    public void updateStoreInfo(StoreUpdateRequest request, Address address) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.storeNumber = request.getStoreNumber();
        this.operatingTime = request.getOperatingTime();
        this.address.delete();
        this.address = address;
    }

    public void changeImages(List<StoreImage> storeImages) {
        // 새로운 이미지로 변경
        this.images = storeImages;
    }

    public void updateCoupon(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public String getThumbnail() {
        return this.images.stream()
                .findFirst()
                .map(StoreImage::getUrl)
                .orElse(null);
    }

    public void deleteImage() {
        this.images = List.of();
    }
}
