package com.connectCo.domain.event.service;

import com.connectCo.domain.event.dto.response.EventInquiryByMemberResponse;

import java.util.List;

public interface EventService {

    List<EventInquiryByMemberResponse> inquiryEventByMember();
}
