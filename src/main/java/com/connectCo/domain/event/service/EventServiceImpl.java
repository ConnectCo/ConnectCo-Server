package com.connectCo.domain.event.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.service.AddressService;
import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
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
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.domain.store.entity.StoreImage;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.global.validation.ParamValidator;
import com.connectCo.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final EventLikeRepository eventLikeRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventLikeMapper eventLikeMapper;

    private final EventImageService eventImageService;
    private final AuthService authService;
    private final AddressService addressService;
    private final OrganizationService organizationService;

    /*
     * 이벤트 생성
     */
    @Override
    @Transactional
    public EventIdResponse createEvent(List<MultipartFile> eventImages, EventCreateRequest request) {
        Member member = authService.getLoginMember();
        Address newAddress = addressService.createAddress(
                request.getDetailAddress(), request.getLatitude(), request.getLongitude());
        Event newEvent = createAndSaveEvent(member, request, newAddress);

        // 조직을 선택했다면, 조직 넣어주기
        if (request.getOrganizationId() != null) {
            Organization organization = organizationService.loadOrganization(request.getOrganizationId());
            newEvent.setOrganization(organization);
        }

        // 이미지가 존재한다면, 이미지 넣어주기
        if (eventImages != null) {
            List<EventImage> newEventImages = eventImageService.createAndSaveEventImage(newEvent, eventImages);
            newEvent.changeImages(newEventImages);
        }

        return new EventIdResponse(newEvent.getId());
    }

    @Override// 이벤트 수정
    @Transactional
    public EventIdResponse updateEvent(Long eventId, List<MultipartFile> newImages, EventUpdateRequest request) {
        Member member = authService.getLoginMember();
        Event event = loadEvent(eventId);

        // 수정 권한 유효성 검사(본인이 아닌 경우 수정 불가)
        ParamValidator.validModify(member.getId(), event.getMember().getId());

        Address newAddress = addressService.createAddress(
                request.getDetailAddress(), request.getLatitude(), request.getLongitude());
        event.updateEventInfo(request, newAddress);

        // 이미지 업데이트
        eventImageService.updateEventImages(event, request.getExistingImages(), newImages);

        return new EventIdResponse(event.getId());
    }

    @Override//이벤트 삭제
    @Transactional
    public Long deleteEvent(Long eventId){
        Member member = authService.getLoginMember();

        Event event= eventRepository.findById(eventId).orElseThrow(() -> new CustomApiException(ErrorCode.EVENT_NOT_FOUND));

        if(!event.getMember().equals(member)){
            throw new CustomApiException(ErrorCode.INVALID_PERMISSION);
        }

        Long deletedEventId = event.getId();
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
    public EventDetailInquiryResponse inquiryEventDetailByEventId(Long eventId){
        Optional<Event> event = eventRepository.findById(eventId);
        return event
                .map(eventMapper::toEventDetailInquiryResponse)
                .orElseThrow(null);
    }

    @Override
    @Transactional
    public EventLikeResponse likeEvent(Long eventId){
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

    private Event createAndSaveEvent(Member member, EventCreateRequest request, Address address) {
        Event event = eventMapper.toEvent(member, request, address);
        return eventRepository.save(event);
    }

    @Override
    public Event loadEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.EVENT_NOT_FOUND));
    }
}
