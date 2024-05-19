package com.connectCo.domain.event.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findAllByMember(Member member);

    @Query("SELECT DISTINCT e FROM Event e JOIN e.organization o WHERE (e.name LIKE%:keyword% OR o.name LIKE%:keyword%) AND e.expiredAt >= :currentTime")
    List<Event> findAllBySearch(String keyword, @Param("currentTime")LocalDateTime currentTime);
    List<Event> findAllByOrderByCreatedAtDesc();

    @Query("SELECT DISTINCT e FROM Event e  WHERE e.expiredAt >= :currentTime GROUP BY e ORDER BY e.likeCount DESC")
    List<Event> findAllByRecommends(@Param("currentTime")LocalDateTime currentTime);

    //Event findById(UUID eventId);
}
