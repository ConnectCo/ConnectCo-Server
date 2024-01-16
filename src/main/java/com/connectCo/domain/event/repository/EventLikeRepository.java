package com.connectCo.domain.event.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.event.entity.EventLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventLikeRepository extends JpaRepository<EventLike, UUID> {
    List<EventLike> findAllByMemberAndIsChecked(Member member, boolean isChecked);
}
