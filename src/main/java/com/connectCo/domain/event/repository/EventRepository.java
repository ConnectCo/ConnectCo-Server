package com.connectCo.domain.event.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findAllByMember(Member member);
}
