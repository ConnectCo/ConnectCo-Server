package com.connectCo.domain.event.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventLike;
import com.connectCo.domain.event.mapper.EventMapper;
import com.connectCo.domain.event.repository.EventLikeRepository;
import com.connectCo.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final AuthService authService;
    private final EventLikeRepository eventLikeRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public UUID getEventId(String eventId) {
        return UUID.fromString(eventId);
    }
    //@Override
    //public Page<EventDetailInquiryResponse> inquiryEventByStartedAt(Pageable pageable) {
    //    Page<Event> eventPage = eventRepository.findAll(pageable);

    //    return eventPage.map(eventMapper::toEventDetailInquiryResponse);
    //}

    @Override
    public List<EventSummaryInquiryResponse> inquiryEventByName(String name){
        // name 이 부분 일치만 해도 검색되게 로직 추가하기

        List<Event> eventList = eventRepository.findAllByName(name);
        return eventList.stream()
                .map(eventMapper::toEventSummaryInquiryResponse)
                .toList();
    }
    @Override
    public EventDetailInquiryResponse inquiryEventByEventId(String eventId){
        UUID eventUuid =getEventId(eventId);
        Optional<Event> optionalEvent = eventRepository.findById(eventUuid);

        return optionalEvent
                .map(eventMapper::toEventDetailInquiryResponse)
                .orElse(null);
    }

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
