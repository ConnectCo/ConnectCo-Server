package com.connectCo.domain.event.service;

import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;

import java.util.List;
import java.util.UUID;

public interface EventService {

    UUID getEventId(String eventId);
    //Page<EventDetailInquiryResponse> inquiryEventByStartedAt();
    EventDetailInquiryResponse inquiryEventByEventId(String eventId);
    List<EventSummaryInquiryResponse> inquiryEventByName(String name);
    List<EventSummaryInquiryResponse> inquiryEventByMember();
    List<EventSummaryInquiryResponse> inquiryEventByLike();
}
