package com.connectCo.domain.event.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByMember(Member member);
    // 키워드로 이벤트 조회
    @Query("SELECT DISTINCT e FROM Event e JOIN e.organization o WHERE " +
            "(e.name LIKE%:keyword% OR o.name LIKE%:keyword% OR e.description LIKE %:keyword%) AND e.expiredAt >= :currentDate")
    Page<Event> findAllBySearch(@Param("keyword") String keyword, @Param("currentDate") LocalDate currentDate, Pageable pageable);
    // 추천순으로 이벤트 조회
    @Query("SELECT e FROM Event e WHERE e.expiredAt >= :currentDate ORDER BY e.likeCount DESC")
    Page<Event> findAllByRecommend(@Param("currentDate") LocalDate currentDate, Pageable pageable);
    // 생성순으로 이벤트 조회
    @Query("SELECT e FROM Event e WHERE e.expiredAt >= :currentDate  ORDER BY e.createdAt DESC")
    Page<Event> findAllByCreatedAt(@Param("currentDate") LocalDate currentDate, Pageable pageable);
    // 거리순으로 이벤트 조회
    @Query("SELECT e FROM Event e JOIN e.address a WHERE e.expiredAt >= :currentDate " +
            "ORDER BY function('ST_Distance_Sphere', point(a.longitude, a.latitude), point(:longitude, :latitude)) ASC")
    Page<Event> findAllByDistance(@Param("latitude") double latitude, @Param("longitude") double longitude,
                                  @Param("currentDate") LocalDate currentDate, Pageable pageable);
}
