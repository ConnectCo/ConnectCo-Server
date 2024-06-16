package com.connectCo.domain.event.repository;

import com.connectCo.domain.event.entity.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
}
