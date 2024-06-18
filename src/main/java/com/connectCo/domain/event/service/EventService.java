package com.connectCo.domain.event.service;

import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventIdResponse;
import com.connectCo.domain.event.dto.response.EventLikeResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import com.connectCo.domain.event.entity.Event;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    EventIdResponse createEvent(List<MultipartFile> eventImages, EventCreateRequest request);
    EventIdResponse updateEvent(Long eventId, List<MultipartFile> newImages, EventUpdateRequest request);
    EventIdResponse deleteEvent(Long eventId);
    EventDetailInquiryResponse inquiryEventDetailByEventId(Long eventId);
    EventLikeResponse likeEvent(Long eventId);
    List<EventSummaryInquiryResponse> inquiryEventByRecent();
    List<EventSummaryInquiryResponse> inquiryEventByRecommends();
    List<EventSummaryInquiryResponse> inquiryEventByKeyword(String keyword);
    List<EventSummaryInquiryResponse> inquiryEventByMember();
    List<EventSummaryInquiryResponse> inquiryEventByLike();
    Event loadEvent(Long eventId);
}
