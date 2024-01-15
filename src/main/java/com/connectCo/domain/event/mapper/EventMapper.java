package com.connectCo.domain.event.mapper;

import com.connectCo.domain.event.dto.response.EventInquiryByMemberResponse;
import com.connectCo.domain.event.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventInquiryByMemberResponse toEventInquiryByMemberResponse(Event event) {
        return EventInquiryByMemberResponse.builder()
                .eventId(event.getId())
                .organizationName(event.getOrganization().getName())
                .name(event.getName())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .build();
    }
}
