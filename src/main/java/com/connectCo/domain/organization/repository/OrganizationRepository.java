package com.connectCo.domain.organization.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    long countByMember(Member member);


    // 키워드로 조직을 검색
    List<Organization> findAllByNameContainingIgnoreCaseOrderByNameAsc(String keyword);
    Optional<Organization> findOrganizationByName(String name);

    boolean existsOrganizationByName(String name);
}
