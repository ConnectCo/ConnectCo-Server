package com.connectCo.domain.event.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.event.entity.Event;
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

//    Page<Event> findAllByMember(Member member, Pageable pageable);
//    // 키워드로 이벤트 조회
//    @Query("SELECT DISTINCT e FROM Event e JOIN e.organization o WHERE " +
//            "(e.name LIKE%:keyword% OR o.name LIKE%:keyword% OR e.description LIKE %:keyword%) AND e.expiredAt >= :currentDate")
//    Page<Event> findAllBySearch(@Param("keyword") String keyword, @Param("currentDate") LocalDate currentDate, Pageable pageable);
//
//    // 추천순으로 이벤트 조회
//    @Query("SELECT e FROM Event e WHERE e.expiredAt >= :currentDate ORDER BY e.likeCount DESC")
//    Page<Event> findAllByRecommend(@Param("currentDate") LocalDate currentDate, Pageable pageable);
//    // 조직 내의 추천순으로 이벤트 조회)
//    @Query("SELECT e FROM Event e WHERE e.organization.id = :organizationId AND e.expiredAt >= :currentDate ORDER BY e.likeCount DESC")
//    Page<Event> findAllByRecommendAndOrganization(@Param("organizationId") Long organizationId,
//                                                  @Param("currentDate") LocalDate currentDate, Pageable pageable);
//
//    // 생성순으로 이벤트 조회
//    @Query("SELECT e FROM Event e WHERE e.expiredAt >= :currentDate  ORDER BY e.createdAt DESC")
//    Page<Event> findAllByCreatedAt(@Param("currentDate") LocalDate currentDate, Pageable pageable);
//    // 조직 내의 생성순으로 이벤트 조회
//    @Query("SELECT e FROM Event e WHERE e.organization.id = :organizationId AND e.expiredAt >= :currentDate ORDER BY e.createdAt DESC")
//    Page<Event> findAllByCreatedAtAndOrganization(@Param("organizationId") Long organizationId,
//                                                  @Param("currentDate") LocalDate currentDate, Pageable pageable);
//
//    // 거리순으로 이벤트 조회
//    @Query("SELECT e FROM Event e JOIN e.address a WHERE e.expiredAt >= :currentDate " +
//            "ORDER BY function('ST_Distance_Sphere', point(a.longitude, a.latitude), point(:longitude, :latitude)) ASC")
//    Page<Event> findAllByDistance(@Param("latitude") double latitude, @Param("longitude") double longitude,
//                                  @Param("currentDate") LocalDate currentDate, Pageable pageable);
//    // 조직 내의 거리순으로 이벤트 조회
//    @Query("SELECT e FROM Event e JOIN e.address a WHERE e.organization.id = :organizationId AND e.expiredAt >= :currentDate " +
//            "ORDER BY function('ST_Distance_Sphere', point(a.longitude, a.latitude), point(:longitude, :latitude)) ASC")
//    Page<Event> findAllByDistanceAndOrganization(@Param("organizationId") Long organizationId, @Param("latitude") double latitude,
//                                                 @Param("longitude") double longitude, @Param("currentDate") LocalDate currentDate, Pageable pageable);
//
//    // 주변 이벤트 조회
//    @Query(value = "SELECT e.*, " +
//            "ST_Distance_Sphere(POINT(:longitude, :latitude), a.location) / 1000 AS distance " +
//            "FROM Event e " +
//            "JOIN Address a ON e.address.id = a.id " +
//            "WHERE ST_DWithin(a.location, POINT(:longitude, :latitude), :radius * 1000) " +
//            "AND e.expiredAt >= CURRENT_DATE " +
//            "ORDER BY distance", nativeQuery = true)
//    Page<Object[]> findAllByLocationWithinRadius(@Param("latitude") double latitude,
//                                                 @Param("longitude") double longitude,
//                                                 @Param("radius") double radius,
//                                                 Pageable pageable);
}
