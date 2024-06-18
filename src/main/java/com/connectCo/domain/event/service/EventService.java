package com.connectCo.domain.event.service;

import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
import com.connectCo.domain.event.dto.response.*;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.global.common.enums.InquiryType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    EventIdResponse createEvent(List<MultipartFile> eventImages, EventCreateRequest request);
    EventIdResponse updateEvent(Long eventId, List<MultipartFile> newImages, EventUpdateRequest request);
    EventIdResponse deleteEvent(Long eventId);
    EventPagingResponse inquiryEventByKeyword(String keyword, int page, int size);
    EventDetailInquiryResponse inquiryEventDetailByEventId(Long eventId);
    EventPagingResponse inquiryEvents(InquiryType type, double latitude, double longitude, int page, int size);
    EventLikeResponse likeEvent(Long eventId);
    List<EventSummaryInquiryResponse> inquiryEventByMember();
    List<EventSummaryInquiryResponse> inquiryEventByLike();
    Event loadEvent(Long eventId);
}
