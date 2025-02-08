package com.connectCo.domain.organization.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    default Organization getOrganization(Long organizationId) {
        return findById(organizationId)
            .orElseThrow(() -> new CustomApiException(ErrorCode.ORGANIZATION_NOT_FOUND));
    }

    long countByMember(Member member);
    boolean existsOrganizationByName(String name);

}
