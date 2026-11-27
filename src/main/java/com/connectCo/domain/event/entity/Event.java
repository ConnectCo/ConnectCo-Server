package com.connectCo.domain.event.entity;

import org.hibernate.annotations.BatchSize;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
import com.connectCo.domain.organization.entity.Organization;
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
// 목록에서 연관 엔티티를 건마다 조회하지 않고 IN으로 묶어 조회한다
@BatchSize(size = 100)
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Organization organization;

    @Builder.Default
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventImage> images = new ArrayList<>();

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
}
