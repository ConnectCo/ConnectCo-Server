package com.connectCo.domain.organization.entity;

import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Organization extends Profile {

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn
    private Address address;

    public void updateOrganizationInfo(OrganizationUpdateRequest request) {
        this.name = request.getName();
        this.phoneNumber = request.getPhoneNumber();
    }
}
