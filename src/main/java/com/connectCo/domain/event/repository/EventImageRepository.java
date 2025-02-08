package com.connectCo.domain.event.repository;

import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    List<EventImage> findAllByEvent(Event event);
}
