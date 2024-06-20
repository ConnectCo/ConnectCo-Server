package com.connectCo.domain.address.entity;

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
