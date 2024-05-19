package com.connectCo.domain.event.mapper;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.event.dto.response.EventLikeResponse;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventLike;
import org.springframework.stereotype.Component;

@Component
public class EventLikeMapper {

    public EventLike toEventLike(Member member, Event event){
        return EventLike.builder()
                .member(member)
                .isChecked(true)
                .event(event)
                .build();
    }

    public EventLikeResponse toEventLikeResponse(Member member, Event event){
        return EventLikeResponse.builder()
                .memberId(member.getId())
                .eventId(event.getId())
                .build();
    }
}
