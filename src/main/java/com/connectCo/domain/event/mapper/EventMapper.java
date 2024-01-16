package com.connectCo.domain.event.mapper;

import com.connectCo.domain.event.dto.response.EventInquiryByMemberResponse;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.organization.entity.Organization;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventMapper {

    public EventInquiryByMemberResponse toEventInquiryByMemberResponse(Event event) {
        String organizationName = Optional.ofNullable(event.getOrganization())
                .map(Organization::getName)
                .orElse(null);

        return EventInquiryByMemberResponse.builder()
                .eventId(event.getId())
                .organizationName(organizationName)
                .name(event.getName())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .build();
    }
}
