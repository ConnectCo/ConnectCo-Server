package com.connectCo.domain.address.entity;

import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String detailAddress;

    private double latitude;

    private double longitude;

    @Column(name = "location", columnDefinition = "POINT")
    private String location;

    @PrePersist
    @PreUpdate
    public void updateLocation() {
        this.location = String.format("POINT(%f %f)", this.longitude, this.latitude);
    }

    public void updateAddress(String detailAddress, double latitude, double longitude) {
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
