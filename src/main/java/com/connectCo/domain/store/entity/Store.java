package com.connectCo.domain.store.entity;

import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.store.dto.request.StoreUpdateRequest;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
public class Store extends Profile {

    private String description;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String operatingTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private Address address;

    @OneToMany(mappedBy = "store")
    private List<Coupon> coupons = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private int appliedEventCount = 0;

    public void updateStoreInfo(StoreUpdateRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.phoneNumber = request.getStoreNumber();
        this.operatingTime = request.getOperatingTime();
    }

    public void updateCoupon(List<Coupon> coupons) {
        this.coupons = coupons;
    }
}
