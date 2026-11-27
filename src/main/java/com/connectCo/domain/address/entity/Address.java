package com.connectCo.domain.address.entity;

import org.hibernate.annotations.BatchSize;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
// 목록에서 연관 엔티티를 건마다 조회하지 않고 IN으로 묶어 조회한다
@BatchSize(size = 100)
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String detailAddress;

    private double latitude;

    private double longitude;

    @Column(name = "location", columnDefinition = "POINT")
    private Point location;

    @PrePersist
    @PreUpdate
    public void updateLocation() {
        GeometryFactory geometryFactory = new GeometryFactory();
        this.location = geometryFactory.createPoint(new Coordinate(this.longitude, this.latitude));
    }

    public void updateAddress(String detailAddress, double latitude, double longitude) {
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        updateLocation();
    }
}
