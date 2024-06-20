package com.connectCo.domain.event.entity;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.sponsorship.entity.Sponsorship;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

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

    @Column(nullable = false)
    @Builder.Default
    private Integer likeCount = 0;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Organization organization;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventCoupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventImage> images = new ArrayList<>();

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void updateEventInfo(EventUpdateRequest request) {
        this.name = request.getName();
        this.startAt = request.getStartAt();
        this.endAt = request.getEndAt();
        this.expiredAt = request.getExpiredAt();
        this.benefitTarget = request.getBenefitTarget();
        this.notification = request.getNotification();
        this.description = request.getDescription();
        this.priorityTarget = request.getPriorityTarget();
    }

    public void changeImages(List<EventImage> eventImages) {
        // 새로운 이미지로 변경
        this.images = eventImages;
    }

    public String getThumbnail() {
        return this.images.stream()
                .findFirst()
                .map(EventImage::getUrl)
                .orElse(null);
    }

    public String getOrganizationName() {
        return Optional.ofNullable(organization)
                .map(Organization::getName)
                .orElse(null);
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
