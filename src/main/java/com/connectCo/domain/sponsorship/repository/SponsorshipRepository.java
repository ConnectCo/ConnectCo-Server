package com.connectCo.domain.sponsorship.repository;

import com.connectCo.domain.sponsorship.entity.Sponsorship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorshipRepository extends JpaRepository<Sponsorship,Long> {
}
