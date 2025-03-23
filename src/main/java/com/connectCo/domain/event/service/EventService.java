package com.connectCo.domain.event.service;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
import com.connectCo.domain.event.dto.response.*;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventSearchType;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.global.common.enums.InquiryType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {

    EventIdResponse createEvent(
        Long profileId, List<MultipartFile> eventImages, EventCreateRequest request
    );
    EventIdResponse updateEvent(
        Long profileId, Long eventId, List<MultipartFile> newImages, EventUpdateRequest request
    );
    EventIdResponse deleteEvent(Long profileId, Long eventId);
    Boolean likeEvent(Long profileId, Long eventId);
    EventDetailInquiryResponse inquiryEventDetail(
        Long profileId, ProfileType profileType, Long eventId
    );
    EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByLike(Long profileId, int page, int size);
    EventPagingResponse<EventSummaryInquiryResponse> inquiryMyEvents(Long profileId, int page, int size);
    EventPagingResponse<EventSummaryInquiryResponse> inquiryEventsByOrganization(Long organizationId, int page, int size);
    EventPagingResponse<EventSummaryInquiryResponse> inquiryEvents(
        PrincipalDetails principal, EventSearchType type, Double latitude, Double longitude, int page, int size
    );
    EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByKeyword(String keyword, int page, int size);
    Event loadEvent(Long eventId);
}
