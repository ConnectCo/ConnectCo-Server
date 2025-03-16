package com.connectCo.domain.event.service;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.entity.CouponSearchType;
import com.connectCo.domain.event.entity.EventSearchType;
import com.connectCo.domain.member.entity.Profile;
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
import com.connectCo.domain.member.repository.ProfileRepository;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.global.validation.ParamValidator;
import java.util.Optional;
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
    private final ProfileRepository profileRepository;

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

    /*
     * 이벤트 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public EventPagingResponse<EventSummaryInquiryResponse> inquiryEvents(
        PrincipalDetails principal, EventSearchType type, Double latitude, Double longitude, int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // 비로그인 시에는 latitude, longitude가 반드시 존재해야 함.
        if (principal == null) {
            if (latitude == null || longitude == null) {
                throw new CustomApiException(ErrorCode.IS_MUST_INPUT_LOCATION);
            }
            ParamValidator.validLocation(latitude, longitude);
            return getEventsByLocation(latitude, longitude, type, pageable);
        }

        // 로그인한 경우
        Optional<Profile> profileOptional =
            profileRepository.findByIdAndProfileType(principal.profileId(), principal.profileType());

        // latitude, longitude가 주어지면 해당 위치 기준으로 조회
        if (latitude != null && longitude != null) {
            ParamValidator.validLocation(latitude, longitude);
            return getEventsByLocation(latitude, longitude, type, pageable);
        }

        // latitude, longitude가 없는 경우, profile에서 address 정보 가져오기
        Address address = getAddressFromProfile(profileOptional.orElse(null));
        if (address == null) {
            throw new CustomApiException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        return getEventsByLocation(address.getLatitude(), address.getLongitude(), type, pageable);

    }

    private Address getAddressFromProfile(Profile profile) {
        if (profile == null) {
            return null;
        }

        if (profile instanceof Store store) {
            return store.getAddress();
        } else if (profile instanceof Organization organization) {
            return organization.getAddress();
        }
        return null;
    }

    private EventPagingResponse<EventSummaryInquiryResponse> getEventsByLocation(
        double latitude, double longitude, EventSearchType type, Pageable pageable) {

        Page<Event> eventPage = switch (type) {
            case RECENCY -> eventRepository.findAllByOrderByCreatedAtDesc(pageable);
            case DISTANCE -> eventRepository.findByDistance(latitude, longitude, pageable);
            case DEADLINE -> eventRepository.findAllByOrderByExpiredAtAsc(pageable);
            default -> throw new CustomApiException(ErrorCode.COUPON_SEARCH_TYPE_INVALID);
        };

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

    @Override
    public Event loadEvent(Long eventId) {
        return eventRepository.getEvent(eventId);
    }

    private Event createAndSaveEvent(Organization organization, EventCreateRequest request, Address address) {
        Event event = eventMapper.toEvent(organization, request, address);
        return eventRepository.save(event);
    }
}
