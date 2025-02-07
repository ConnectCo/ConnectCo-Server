package com.connectCo.domain.event.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventLikeRepository extends JpaRepository<EventLike, Long> {

//    @Query("SELECT el.event FROM EventLike el WHERE el.member = :member AND el.isChecked = :isChecked")
//    Page<Event> findAllEventsByMemberAndIsChecked(@Param("member") Member member, @Param("isChecked") boolean isChecked, Pageable pageable);
//
//    Optional<EventLike> findByMemberAndEvent(Member member, Event event);
}
