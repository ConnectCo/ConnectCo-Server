package com.connectCo.domain.organization.repository;

import com.connectCo.domain.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    // 키워드로 조직을 검색
    List<Organization> findAllByNameContainingIgnoreCaseOrderByNameAsc(String keyword);

    boolean existsOrganizationByName(String name);
}
