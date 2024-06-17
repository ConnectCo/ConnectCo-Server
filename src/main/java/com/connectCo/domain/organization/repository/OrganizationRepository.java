package com.connectCo.domain.organization.repository;

import com.connectCo.domain.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
