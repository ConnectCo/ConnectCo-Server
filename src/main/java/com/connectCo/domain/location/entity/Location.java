package com.connectCo.domain.location.entity;

import com.connectCo.domain.member.entity.Profile;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;//지번 혹은 도로명 주소

    private String detailAddress;

    private double latitude;

    private double longitude;

    @Column
    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Profile profile;
}
