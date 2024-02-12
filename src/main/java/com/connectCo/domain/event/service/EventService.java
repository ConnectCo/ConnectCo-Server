package com.connectCo.domain.event.service;

import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventIdResponse;
import com.connectCo.domain.event.dto.response.EventLikeResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface EventService {

    EventIdResponse createEvent(List<MultipartFile> eventImages, EventCreateRequest request);


    EventSummaryInquiryResponse updateEvent(UUID eventId, EventCreateRequest request, List<MultipartFile> eventImages);
    EventDetailInquiryResponse inquiryEventDetailByEventId(UUID eventId);
    UUID deleteEvent(UUID eventId);

    EventLikeResponse likeEvent(UUID eventId);
    List<EventSummaryInquiryResponse> inquiryEventByRecent();
    List<EventSummaryInquiryResponse> inquiryEventByRecommends();
    List<EventSummaryInquiryResponse> inquiryEventByName(String name);
    List<EventSummaryInquiryResponse> inquiryEventByMember();
    List<EventSummaryInquiryResponse> inquiryEventByLike();


}
