package com.connectCo.domain.event.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventLike;
import com.connectCo.domain.event.mapper.EventMapper;
import com.connectCo.domain.event.repository.EventLikeRepository;
import com.connectCo.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final AuthService authService;
    private final EventLikeRepository eventLikeRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    @Override
    public List<EventSummaryInquiryResponse> inquiryEventByMember() {

        Member member = authService.getLoginMember();

        List<Event> eventList = eventRepository.findAllByMember(member);

        return eventList.stream()
                .map(eventMapper::toEventSummaryInquiryResponse)
                .toList();
    }

    @Override
    public List<EventSummaryInquiryResponse> inquiryEventByLike() {

        Member member = authService.getLoginMember();

        List<Event> eventList = eventLikeRepository.findAllByMemberAndIsChecked(member, true).stream()
                        .map(EventLike::getEvent)
                        .toList();

        return eventList.stream()
                .map(eventMapper::toEventSummaryInquiryResponse)
                .toList();
    }
}
