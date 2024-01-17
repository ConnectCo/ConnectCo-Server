package com.connectCo.domain.event.service;

import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;

import java.util.List;

public interface EventService {

    List<EventSummaryInquiryResponse> inquiryEventByMember();
    List<EventSummaryInquiryResponse> inquiryEventByLike();
}
