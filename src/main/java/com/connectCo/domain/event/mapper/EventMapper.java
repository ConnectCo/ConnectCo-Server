package com.connectCo.domain.event.mapper;

import com.connectCo.domain.event.entity.EventLike;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventLocationInquiryResponse;
import com.connectCo.domain.event.dto.response.EventPagingResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventImage;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.global.common.mapper.CommonMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventMapper {

    public Event toEvent(Organization organization, EventCreateRequest request, Address address){
        return Event.builder()
            .name(request.getName())
            .startAt(request.getStartAt())
            .endAt(request.getEndAt())
            .expiredAt(request.getExpiredAt())
            .benefitTarget(request.getBenefitTarget())
            .notification(request.getNotification())
            .description(request.getDescription())
            .priorityTarget(request.getPriorityTarget())
            .address(address)
            .organization(organization)
            .build();
    }

    public EventImage toEventImage(Event event, String url) {
        return EventImage.builder()
            .event(event)
            .url(url)
            .build();
    }

    public EventLike toEventLike(Store store, Event event) {
        return EventLike.builder()
            .event(event)
            .store(store)
            .isActive(true)
            .build();
    }

    public <T> EventPagingResponse<T> toEventPagingResponse(Page<T> events) {
        return EventPagingResponse.<T>builder()
                .events(events.getContent())
                .page(events.getNumber())
                .totalPages(events.getTotalPages())
                .totalElements((int) events.getTotalElements())
                .isFirst(events.isFirst())
                .isLast(events.isLast())
                .build();
    }


    public EventDetailInquiryResponse toEventDetailInquiryResponse(
        Event event, boolean isLike, boolean isMine
    ) {
        return EventDetailInquiryResponse.builder()
            .eventId(event.getId())
            .organization(toOrganizationInfo(event.getOrganization()))
            .name(event.getName())
            .description(event.getDescription())
            .startAt(event.getStartAt())
            .endAt(event.getEndAt())
            .expiredAt(event.getExpiredAt())
            .benefitTarget(event.getBenefitTarget())
            .priorityTarget(event.getPriorityTarget())
            .address(CommonMapper.toAddressResponse(event.getAddress()))
            .notification(event.getNotification())
            .images(toImageUrls(event.getImages()))
            .isLike(isLike)
            .isMine(isMine)
            .build();
    }

    private EventDetailInquiryResponse.OrganizationInfo toOrganizationInfo(Organization organization) {
        return new EventDetailInquiryResponse.OrganizationInfo(
            organization.getId(),
            organization.getName()
        );
    }


    public EventSummaryInquiryResponse toEventSummaryInquiryResponse(Event event) {
        return EventSummaryInquiryResponse.builder()
                .eventId(event.getId())
                .organizationName(event.getOrganization().getName())
                .name(event.getName())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .thumbnail(event.getThumbnail())
                .build();
    }

    public EventLocationInquiryResponse toEventLocationInquiryResponse(Object[] eventWithDistance) {
        Event event = (Event) eventWithDistance[0];
        Double distance = (Double) eventWithDistance[1];
        return EventLocationInquiryResponse.builder()
                .eventId(event.getId())
                .organizationName(event.getOrganization().getName())
                .name(event.getName())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .thumbnail(event.getThumbnail())
                .latitude(event.getAddress().getLatitude())
                .longitude(event.getAddress().getLongitude())
                .distance(distance)
                .build();
    }

    private List<String> toImageUrls(List<EventImage> images) {
        return images.stream().map(EventImage::getUrl).toList();
    }
}
