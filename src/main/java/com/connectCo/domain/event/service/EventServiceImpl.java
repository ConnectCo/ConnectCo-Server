package com.connectCo.domain.event.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventIdResponse;
import com.connectCo.domain.event.dto.response.EventLikeResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventImage;
import com.connectCo.domain.event.entity.EventLike;
import com.connectCo.domain.event.mapper.EventLikeMapper;
import com.connectCo.domain.event.mapper.EventMapper;
import com.connectCo.domain.event.repository.EventImageRepository;
import com.connectCo.domain.event.repository.EventLikeRepository;
import com.connectCo.domain.event.repository.EventRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final AuthService authService;
    private final EventLikeRepository eventLikeRepository;
    private final EventRepository eventRepository;
    private final EventImageRepository eventImageRepository;
    private final EventMapper eventMapper;
    private final EventLikeMapper eventLikeMapper;
    private final S3FileComponent s3FileComponent;

    @Override// 이벤트 생성
    @Transactional
    public EventIdResponse createEvent(List<MultipartFile> eventImages, EventCreateRequest request){
        Member member = authService.getLoginMember();

        Event newEvent = createAndSaveEvent(member, request);

        List<EventImage> newEventImages = createAndSaveEventImages(newEvent, eventImages);

        newEvent.changeImages(newEventImages);

        return new EventIdResponse(newEvent.getId());
    }

    @Override// 이벤트 수정
    @Transactional
    public EventSummaryInquiryResponse updateEvent(UUID eventId, EventCreateRequest request, List<MultipartFile> eventImages){

        Event existingEvent = eventRepository.findById(eventId).orElseThrow(() -> new CustomApiException(ErrorCode.EVENT_NOT_FOUND));

        List<EventImage> updateEventImages = updateAndSaveEventImages(existingEvent, eventImages);

        existingEvent.changeImages(updateEventImages);


        return eventMapper.toEventSummaryInquiryResponse(eventRepository.save(existingEvent));
    }
    @Override//이벤트 삭제
    @Transactional
    public UUID deleteEvent(UUID eventId){
        Member member = authService.getLoginMember();

        Event event= eventRepository.findById(eventId).orElseThrow(() -> new CustomApiException(ErrorCode.EVENT_NOT_FOUND));

        if(!event.getMember().equals(member)){
            throw new CustomApiException(ErrorCode.INVALID_PERMISSION);
        }

        UUID deletedEventId = event.getId();
        eventRepository.deleteById(deletedEventId);
        return deletedEventId;
    }
    @Override//이벤트 검색하기 우선 학교 이름과 이벤트 제목 둘다에서 검색 되게 해놨습니다.
    @Transactional
    public List<EventSummaryInquiryResponse> inquiryEventByKeyword(String keyword){
        LocalDateTime currentTime = LocalDateTime.now();
        List<Event> eventList = eventRepository.findAllBySearch(keyword, currentTime);
        return eventList.stream()
                .map(eventMapper::toEventSummaryInquiryResponse)
                .toList();
    }

    @Override//이벤트 최신순 조회하기
    @Transactional
    public List<EventSummaryInquiryResponse> inquiryEventByRecent(){
        List<Event> eventList = eventRepository.findAllByOrderByCreatedAtDesc();

        return eventList.stream()
                .map(eventMapper::toEventSummaryInquiryResponse)
                .toList();
    }
    @Override
    @Transactional
    public List<EventSummaryInquiryResponse> inquiryEventByRecommends(){
        LocalDateTime currentTime = LocalDateTime.now();
        List<Event> eventList = eventRepository.findAllByRecommends(currentTime);

        return eventList.stream()
                .map(eventMapper::toEventSummaryInquiryResponse)
                .toList();
    }

    @Override//이벤트 상세
    @Transactional
    public EventDetailInquiryResponse inquiryEventDetailByEventId(UUID eventId){
        Optional<Event> event = eventRepository.findById(eventId);
        return event
                .map(eventMapper::toEventDetailInquiryResponse)
                .orElseThrow(null);
    }

    @Override
    @Transactional
    public EventLikeResponse likeEvent(UUID eventId){
        Member member = authService.getLoginMember();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new CustomApiException(ErrorCode.EVENT_NOT_FOUND));

        Optional <EventLike> existingLike = eventLikeRepository.findAllByMemberAndEvent(member, event);

        if(existingLike.isPresent()) // 이미 좋아요 누른 상태라면 좋아요 취소
        {
            EventLike eventLike = existingLike.get();
            event.decreaseLikeCount();
            eventLike.changeIsChecked();
            eventLikeRepository.save(eventLike);
        }
        else// 좋아요 누른 적이 없으면 좋아요 증가
        {
            event.increaseLikeCount();
            EventLike newEventLike = eventLikeMapper.toEventLike(member, event);
            eventLikeRepository.save(newEventLike);
        }

        return eventLikeMapper.toEventLikeResponse(member, event);
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

    private Event createAndSaveEvent(Member member, EventCreateRequest request) {
        Event event = eventMapper.toEvent(member, request);
        return eventRepository.save(event);
    }


    private List<EventImage> createAndSaveEventImages(Event newEvent, List<MultipartFile> eventImages) {
        return eventImages.stream()
                .map(eventImage -> s3FileComponent.uploadFile("event", eventImage))
                .map(eventUrl -> eventMapper.toEventImage(newEvent, eventUrl))
                .map(eventImageRepository::save)
                .toList();
    }

    private List<EventImage> updateAndSaveEventImages(Event updateEvent, List<MultipartFile> eventImages) {
        return eventImages.stream()
                .map(eventImage -> s3FileComponent.uploadFile("event", eventImage))
                .map(eventUrl -> eventMapper.toEventImage(updateEvent, eventUrl))
                .map(eventImageRepository::save)
                .toList();
    }
}
