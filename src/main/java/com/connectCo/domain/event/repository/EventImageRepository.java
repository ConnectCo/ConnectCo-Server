package com.connectCo.domain.event.repository;

import com.connectCo.domain.event.entity.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventImageRepository extends JpaRepository<EventImage, UUID> {
}
