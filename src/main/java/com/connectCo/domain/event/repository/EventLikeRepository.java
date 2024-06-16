package com.connectCo.domain.event.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
    List<EventLike> findAllByMemberAndIsChecked(Member member, boolean isChecked);

    Optional<EventLike> findAllByMemberAndEvent(Member member, Event event);
}
