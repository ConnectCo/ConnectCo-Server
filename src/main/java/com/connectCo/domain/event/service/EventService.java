package com.connectCo.domain.event.service;

import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventIdResponse;
import com.connectCo.domain.event.dto.response.EventLikeResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    EventIdResponse createEvent(List<MultipartFile> eventImages, EventCreateRequest request);
    EventSummaryInquiryResponse updateEvent(Long eventId, EventCreateRequest request, List<MultipartFile> eventImages);
    EventDetailInquiryResponse inquiryEventDetailByEventId(Long eventId);
    Long deleteEvent(Long eventId);

    EventLikeResponse likeEvent(Long eventId);
    List<EventSummaryInquiryResponse> inquiryEventByRecent();
    List<EventSummaryInquiryResponse> inquiryEventByRecommends();
    List<EventSummaryInquiryResponse> inquiryEventByKeyword(String keyword);
    List<EventSummaryInquiryResponse> inquiryEventByMember();
    List<EventSummaryInquiryResponse> inquiryEventByLike();


}
