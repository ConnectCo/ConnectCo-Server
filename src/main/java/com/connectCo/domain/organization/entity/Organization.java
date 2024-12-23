package com.connectCo.domain.organization.entity;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
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
public class Organization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn
    private Address address;

    @Column(nullable = false)
    private String homepageUrl;

    @Column(nullable = false)
    private String academicDayUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    public void updateOrganizationInfo(OrganizationUpdateRequest request) {
        this.name = request.getName();
        this.homepageUrl = request.getHomepageUrl();
        this.academicDayUrl = request.getAcademicDayUrl();
    }
}
