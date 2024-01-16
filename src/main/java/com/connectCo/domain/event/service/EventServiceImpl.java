package com.connectCo.domain.event.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.event.dto.response.EventInquiryByMemberResponse;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.mapper.EventMapper;
import com.connectCo.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final AuthService authService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    @Override
    public List<EventInquiryByMemberResponse> inquiryEventByMember() {

        Member member = authService.getLoginMember();

        List<Event> eventList = eventRepository.findAllByMember(member);

        return eventList.stream()
                .map(eventMapper::toEventInquiryByMemberResponse)
                .toList();
    }
}
