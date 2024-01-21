package com.connectCo.domain.event.mapper;

import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventImage;
import com.connectCo.domain.organization.entity.Organization;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventMapper {

    public EventDetailInquiryResponse toEventDetailInquiryResponse(Event event){
        String organizationName = Optional.ofNullable(event.getOrganization())
                .map(Organization::getName)
                .orElse(null);

        String thumbnail = event.getImages().stream()
                .findFirst()
                .map(EventImage::getUrl)
                .orElse(null);

        return EventDetailInquiryResponse.builder()
                .eventId(event.getId())
                .organizationName(organizationName)
                .name(event.getName())
                .description(event.getDescription())
                .priorityTarget(event.getPriorityTarget())
                .benefitTarget(event.getBenefitTarget())
                .notification(event.getNotification())
                .expiredAt(event.getExpiredAt())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .thumbnail(thumbnail)
                .build();
    }


    public EventSummaryInquiryResponse toEventSummaryInquiryResponse(Event event) {
        String organizationName = Optional.ofNullable(event.getOrganization())
                .map(Organization::getName)
                .orElse(null);

        String thumbnail = event.getImages().stream()
                .findFirst()
                .map(EventImage::getUrl)
                .orElse(null);

        return EventSummaryInquiryResponse.builder()
                .eventId(event.getId())
                .organizationName(organizationName)
                .name(event.getName())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .thumbnail(thumbnail)
                .build();
    }
}
