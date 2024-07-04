package com.connectCo.domain.event.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.service.AddressService;
import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
import com.connectCo.domain.event.dto.response.*;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventImage;
import com.connectCo.domain.event.entity.EventLike;
import com.connectCo.domain.event.mapper.EventLikeMapper;
import com.connectCo.domain.event.mapper.EventMapper;
import com.connectCo.domain.event.repository.EventLikeRepository;
import com.connectCo.domain.event.repository.EventRepository;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.global.common.enums.InquiryType;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.global.validation.ParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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

    private Event createAndSaveEvent(Member member, EventCreateRequest request, Address address) {
        Event event = eventMapper.toEvent(member, request, address);
        return eventRepository. save(event);
    }

    /*
     * 이벤트 정보 수정
     */
    @Override
    @Transactional
    public EventIdResponse updateEvent(Long eventId, List<MultipartFile> newImages, EventUpdateRequest request) {
        Member member = authService.getLoginMember();
        Event event = loadEvent(eventId);

        // 수정 권한 유효성 검사(본인이 아닌 경우 수정 불가)
        ParamValidator.validModify(member.getId(), event.getMember().getId());

        event.getAddress().updateAddress(request.getDetailAddress(), request.getLatitude(), request.getLongitude());
        event.updateEventInfo(request);

        // 조직을 새로 추가했거나 기존 조직 그대로인 경우
        if (request.getOrganizationId() != null) {
            Organization organization = organizationService.loadOrganization(request.getOrganizationId());
            event.setOrganization(organization);
        }

        // 이미지 업데이트
        eventImageService.updateEventImages(event, request.getExistingImages(), newImages);

        return new EventIdResponse(event.getId());
    }

    /*
     * 특정 이벤트 삭제
     */
    @Override
    @Transactional
    public EventIdResponse deleteEvent(Long eventId){
        Member member = authService.getLoginMember();
        Event event = loadEvent(eventId);

        // 삭제 권한 유효성 검사(본인이 아닌 경우 삭제 불가)
        ParamValidator.validModify(member.getId(), event.getMember().getId());

        // 이벤트 이미지 삭제
        eventImageService.deleteExistingImages(event.getImages());
        event.changeImages(List.of());

        Long deletedEventId = event.getId();
        // 이벤트 soft 삭제
        event.delete();

        return new EventIdResponse(deletedEventId);
    }

    /*
     * 특정 가게 찜하기
     */
    @Override
    @Transactional
    public Boolean likeEvent(Long eventId){
        Member member = authService.getLoginMember();
        Event event = loadEvent(eventId);

        Optional <EventLike> eventLike = eventLikeRepository.findByMemberAndEvent(member, event);

        return eventLike.map(EventLike::changeIsChecked)
                .orElseGet(() -> eventLikeRepository.save(eventLikeMapper.toEventLike(member, event)).isChecked());
    }

    /*
     * 특정 이벤트 상세 조회
     */
    @Override
    public EventDetailInquiryResponse inquiryEventDetailByEventId(Long eventId){
        Event event = loadEvent(eventId);
        return eventMapper.toEventDetailInquiryResponse(event);
    }


    /*
     * 이벤트 검색
     */
    @Override
    public EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByKeyword(String keyword, int page, int size){
        LocalDate currentDate = LocalDate.now();
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.findAllBySearch(keyword, currentDate, pageable);
        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
    }

    /*
     * 조건에 따른 이벤트 조회
     */
    @Override
    public EventPagingResponse<EventSummaryInquiryResponse> inquiryEvents(InquiryType type, Long organizationId, double latitude, double longitude, int page, int size){
        LocalDate currentDate = LocalDate.now();
        Pageable pageable = PageRequest.of(page, size);

        return switch (type) {
            case RECOMMEND -> inquiryEventByRecommend(organizationId, currentDate, pageable);
            case RECENT -> inquiryEventByCreateAt(organizationId, currentDate, pageable);
            case DISTANCE -> {
                // 위도, 경도 데이터 유효성 검사
                ParamValidator.validLocation(latitude, longitude);
                yield inquiryEventByDistance(organizationId, latitude, longitude, currentDate, pageable);
            }
            default -> throw new CustomApiException(ErrorCode.UNKNOWN_INQUIRY_TYPE);
        };
    }
    private EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByRecommend(Long organizationId, LocalDate currentDate, Pageable pageable){
        Page<Event> eventPage = organizationId != null ?
                eventRepository.findAllByRecommendAndOrganization(organizationId, currentDate, pageable) :
                eventRepository.findAllByRecommend(currentDate, pageable);
        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
    }

    private EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByCreateAt(Long organizationId, LocalDate currentDate, Pageable pageable){
        Page<Event> eventPage = organizationId != null ?
                eventRepository.findAllByCreatedAtAndOrganization(organizationId, currentDate, pageable) :
                eventRepository.findAllByCreatedAt(currentDate, pageable);
        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
    }

    private EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByDistance(Long organizationId, double latitude, double longitude, LocalDate currentDate, Pageable pageable) {
        Page<Event> eventPage = organizationId != null ?
                eventRepository.findAllByDistanceAndOrganization(organizationId,latitude, longitude, currentDate, pageable) :
                eventRepository.findAllByDistance(latitude, longitude, currentDate, pageable);
        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
    }

    /*
     * 나의 이벤트 조회
     */
    @Override
    public EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByMember(int page, int size) {
        Member member = authService.getLoginMember();
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventRepository.findAllByMember(member, pageable);
        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
    }

    /*
     * 내가 찜한 이벤트 조회
     */
    @Override
    public EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByLike(int page, int size) {
        Member member = authService.getLoginMember();
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventLikeRepository.findAllEventsByMemberAndIsChecked(member, true, pageable);
        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
    }

    /*
     * 내 주변 이벤트 목록 조회
     */
    @Override
    public EventPagingResponse<EventLocationInquiryResponse> inquiryEventByLocation(double latitude, double longitude, int radius, int page, int size) {
        // 위도, 경도, 반경 값 유효성 검사
        ParamValidator.validLocation(latitude, longitude);
        ParamValidator.validRadius(radius);

        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> eventPage = eventRepository.findAllByLocationWithinRadius(latitude, longitude, radius, pageable);
        Page<EventLocationInquiryResponse> mappedPage = eventPage.map(eventMapper::toEventLocationInquiryResponse);
        return eventMapper.toEventPagingResponse(mappedPage);
    }

    @Override
    public Event loadEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.EVENT_NOT_FOUND));
    }
}
