package com.connectCo.domain.event.entity;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.global.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

    @Column(nullable = false)
    private LocalDate expiredAt;

    @Column(nullable = false)
    private String benefitTarget;

    @Column(nullable = false)
    private String notification;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String priorityTarget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Organization organization;

    @OneToMany(mappedBy = "event")
    private List<EventCoupon> coupons;

    @OneToMany(mappedBy = "event")
    private List<EventImage> images;
}
