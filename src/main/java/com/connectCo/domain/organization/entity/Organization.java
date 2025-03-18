package com.connectCo.domain.organization.entity;

import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
public class Organization extends Profile {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn
    private Address address;

    @OneToMany(mappedBy = "organization")
    @Builder.Default
    private List<Event> events = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private int appliedCouponCount = 0;

    @Enumerated(EnumType.STRING)
    private UniversityVerificationStatus verificationStatus;

    private LocalDateTime verifiedAt;

    private String universityName;

    public void updateOrganizationInfo(OrganizationUpdateRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.phoneNumber = request.getPhoneNumber();
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void removeEvent(Event event) {
        this.events.remove(event);
    }

    public void setPendingVerification(String universityName) {
        this.universityName = universityName;
        this.verificationStatus = UniversityVerificationStatus.PENDING;
    }

    public void setVerified(LocalDateTime verifiedAt) {
        this.verificationStatus = UniversityVerificationStatus.VERIFIED;
        this.verifiedAt = verifiedAt;
    }

    public boolean isVerified() {
        return this.verificationStatus == UniversityVerificationStatus.VERIFIED;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
