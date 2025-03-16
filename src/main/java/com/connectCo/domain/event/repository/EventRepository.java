package com.connectCo.domain.event.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface EventRepository extends JpaRepository<Event, Long> {

    default Event getEvent(Long eventId) {
        return findById(eventId)
            .orElseThrow(() -> new CustomApiException(ErrorCode.EVENT_NOT_FOUND));
    }

    Page<Event> findAllByOrganization(Organization organization, Pageable pageable);
    Page<Event> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Event> findAllByOrderByExpiredAtAsc(Pageable pageable);
    @Query(value = """
        SELECT e.* FROM event e
        JOIN organization o ON e.organization_id = o.id
        JOIN address a ON o.address_id = a.id
        ORDER BY ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(a.longitude, a.latitude))
    """, nativeQuery = true)
    Page<Event> findByDistance(
        @Param("latitude") double latitude, @Param("longitude") double longitude, Pageable pageable
    );

}
