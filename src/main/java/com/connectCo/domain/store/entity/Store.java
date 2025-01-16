package com.connectCo.domain.store.entity;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.store.dto.request.StoreUpdateRequest;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Store extends Profile {

    private String description;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String operatingTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @OneToMany(mappedBy = "store")
    private List<StoreImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Coupon> coupons = new ArrayList<>();

    public void updateStoreInfo(StoreUpdateRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.phoneNumber = request.getStoreNumber();
        this.operatingTime = request.getOperatingTime();
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
}
