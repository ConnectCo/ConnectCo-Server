package com.connectCo.domain.organization.repository;

import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.entity.OrganizationLike;
import com.connectCo.domain.store.entity.Store;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationLikeRepository extends JpaRepository<OrganizationLike, Long> {
    Optional<OrganizationLike> findByOrganizationAndStore(Organization organization, Store store);
    Page<OrganizationLike> findAllByStoreAndIsActiveTrue(Store store, Pageable pageable);
}
