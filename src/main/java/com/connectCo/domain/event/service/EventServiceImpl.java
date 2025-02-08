package com.connectCo.domain.event.service;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.service.AddressService;
import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
import com.connectCo.domain.event.dto.response.*;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventImage;
import com.connectCo.domain.event.mapper.EventMapper;
import com.connectCo.domain.event.repository.EventRepository;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.global.validation.ParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventImageService eventImageService;
    private final EventLikeService eventLikeService;

    private final AddressService addressService;
    private final StoreService storeService;
    private final OrganizationService organizationService;
//
    /*
     * 이벤트 생성
     */
    @Override
    @Transactional
    public EventIdResponse createEvent(
        Long profileId, List<MultipartFile> eventImages, EventCreateRequest request
    ) {
        Organization organization = organizationService.loadOrganization(profileId);

        Address newAddress = addressService.createAddress(
                request.getDetailAddress(), request.getLatitude(), request.getLongitude()
        );
        Event newEvent = createAndSaveEvent(organization, request, newAddress);
        organization.addEvent(newEvent);
        if (eventImages != null) {
            List<EventImage> newEventImages = eventImageService.createAndSaveEventImage(newEvent, eventImages);
            newEvent.changeImages(newEventImages);
        }

        return new EventIdResponse(newEvent.getId());
    }

    /*
     * 이벤트 정보 수정
     */
    @Override
    @Transactional
    public EventIdResponse updateEvent(
        Long profileId, Long eventId, List<MultipartFile> newImages, EventUpdateRequest request
    ) {
        Organization organization = organizationService.loadOrganization(profileId);
        // 수정 권한 유효성 검사(본인이 아닌 경우 수정 불가)
        Event event = eventRepository.getEvent(eventId);
        ParamValidator.validModify(event.getOrganization().getId(), organization.getId());

        // 주소 업데이트 및 이벤트 정보 업데이트
        event.getAddress().updateAddress(
            request.getDetailAddress(), request.getLatitude(), request.getLongitude()
        );
        event.updateEventInfo(request);

        // 이미지 업데이트
        if (newImages != null && !newImages.isEmpty()) {
            eventImageService.updateEventImages(event, request.getExistingImages(), newImages);
        }

        return new EventIdResponse(event.getId());
    }

    /*
     * 특정 이벤트 삭제
     */
    @Override
    @Transactional
    public EventIdResponse deleteEvent(Long profileId, Long eventId) {
        Organization organization = organizationService.loadOrganization(profileId);
        // 삭제 권한 유효성 검사(본인이 아닌 경우 삭제 불가)
        Event event = eventRepository.getEvent(eventId);
        ParamValidator.validModify(event.getOrganization().getId(), organization.getId());

        // 이벤트 이미지 삭제
        eventImageService.deleteExistingImages(event.getImages());
        organization.removeEvent(event);

        // TODO: 관련된 찜 기록, 협찬 기록 등 삭제 로직 추가

        // 이벤트 hard delete
        eventRepository.delete(event);

        return new EventIdResponse(eventId);
    }

    /*
     * 특정 이벤트 찜하기
     */
    @Override
    @Transactional
    public Boolean likeEvent(Long profileId, Long eventId){
        Store store = storeService.loadStore(profileId);
        Event event = eventRepository.getEvent(eventId);

        return eventLikeService.likeEvent(event, store);
    }

    /*
     * 특정 이벤트 상세 조회
     */
    @Override
    public EventDetailInquiryResponse inquiryEventDetail(
        Long profileId, ProfileType profileType, Long eventId
    ){
        Event event = eventRepository.getEvent(eventId);

        // 본인 여부 확인
        Boolean isMine = event.getOrganization().getId().equals(profileId);

        // 찜 여부 확인
        Boolean isLiked = Boolean.FALSE;
        if (profileId != null && profileType != null && profileType.equals(ProfileType.STORE)) {
            Store store = storeService.loadStore(profileId);
            isLiked = eventLikeService.isLikeEvent(event, store);
        }

        return eventMapper.toEventDetailInquiryResponse(event, isLiked, isMine);
    }

    /*
     * 내가 찜한 이벤트 조회
     */
    @Override
    public EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByLike(
        Long profileId, int page, int size
    ) {
        Store store = storeService.loadStore(profileId);

        Page<Event> eventPage = eventLikeService.getEventsByLike(store, PageRequest.of(page, size));
        return eventMapper.toEventPagingResponse(
            eventPage.map(eventMapper::toEventSummaryInquiryResponse)
        );
    }

    /*
     * 나의 이벤트 조회
     */
    public EventPagingResponse<EventSummaryInquiryResponse> inquiryMyEvents(
        Long profileId, int page, int size
    ) {
        Organization organization = organizationService.loadOrganization(profileId);
        Page<Event> eventPage = eventRepository.findAllByOrganization(organization, PageRequest.of(page, size));

        return eventMapper.toEventPagingResponse(
            eventPage.map(eventMapper::toEventSummaryInquiryResponse)
        );
    }
//
//    /*
//     * 이벤트 검색
//     */
//    @Override
//    public EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByKeyword(String keyword, int page, int size){
//        LocalDate currentDate = LocalDate.now();
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Event> eventPage = eventRepository.findAllBySearch(keyword, currentDate, pageable);
//        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
//    }
//
//    /*
//     * 조건에 따른 이벤트 조회
//     */
//    @Override
//    public EventPagingResponse<EventSummaryInquiryResponse> inquiryEvents(InquiryType type, Long organizationId, double latitude, double longitude, int page, int size){
//        LocalDate currentDate = LocalDate.now();
//        Pageable pageable = PageRequest.of(page, size);
//
//        return switch (type) {
//            case RECOMMEND -> inquiryEventByRecommend(organizationId, currentDate, pageable);
//            case RECENT -> inquiryEventByCreateAt(organizationId, currentDate, pageable);
//            case DISTANCE -> {
//                // 위도, 경도 데이터 유효성 검사
//                ParamValidator.validLocation(latitude, longitude);
//                yield inquiryEventByDistance(organizationId, latitude, longitude, currentDate, pageable);
//            }
//            default -> throw new CustomApiException(ErrorCode.UNKNOWN_INQUIRY_TYPE);
//        };
//    }
//    private EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByRecommend(Long organizationId, LocalDate currentDate, Pageable pageable){
//        Page<Event> eventPage = organizationId != null ?
//                eventRepository.findAllByRecommendAndOrganization(organizationId, currentDate, pageable) :
//                eventRepository.findAllByRecommend(currentDate, pageable);
//        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
//    }
//
//    private EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByCreateAt(Long organizationId, LocalDate currentDate, Pageable pageable){
//        Page<Event> eventPage = organizationId != null ?
//                eventRepository.findAllByCreatedAtAndOrganization(organizationId, currentDate, pageable) :
//                eventRepository.findAllByCreatedAt(currentDate, pageable);
//        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
//    }
//
//    private EventPagingResponse<EventSummaryInquiryResponse> inquiryEventByDistance(Long organizationId, double latitude, double longitude, LocalDate currentDate, Pageable pageable) {
//        Page<Event> eventPage = organizationId != null ?
//                eventRepository.findAllByDistanceAndOrganization(organizationId,latitude, longitude, currentDate, pageable) :
//                eventRepository.findAllByDistance(latitude, longitude, currentDate, pageable);
//        return eventMapper.toEventPagingResponse(eventPage.map(eventMapper::toEventSummaryInquiryResponse));
//    }
//
//    /*
//     * 내 주변 이벤트 목록 조회
//     */
//    @Override
//    public EventPagingResponse<EventLocationInquiryResponse> inquiryEventByLocation(double latitude, double longitude, int radius, int page, int size) {
//        // 위도, 경도, 반경 값 유효성 검사
//        ParamValidator.validLocation(latitude, longitude);
//        ParamValidator.validRadius(radius);
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Object[]> eventPage = eventRepository.findAllByLocationWithinRadius(latitude, longitude, radius, pageable);
//        Page<EventLocationInquiryResponse> mappedPage = eventPage.map(eventMapper::toEventLocationInquiryResponse);
//        return eventMapper.toEventPagingResponse(mappedPage);
//    }
//

    // TODO: 추천 이벤트 조회 추가

    @Override
    public Event loadEvent(Long eventId) {
        return eventRepository.getEvent(eventId);
    }

    private Event createAndSaveEvent(Organization organization, EventCreateRequest request, Address address) {
        Event event = eventMapper.toEvent(organization, request, address);
        return eventRepository.save(event);
    }
}
