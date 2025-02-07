package com.connectCo.domain.event.controller;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
import com.connectCo.domain.event.dto.response.*;
import com.connectCo.domain.event.service.EventService;
import com.connectCo.domain.store.dto.response.StoreLocationInquiryResponse;
import com.connectCo.global.common.BaseResponse;
import com.connectCo.global.common.enums.InquiryType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "이벤트 API", description = "이벤트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "이벤트 생성 API", description = "조직 프로필만 등록 가능")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<EventIdResponse> createEvent (
        @AuthenticationPrincipal PrincipalDetails principal,
        @Parameter(description = "이벤트 이미지 파일들(없을 시 사용 x)") @RequestPart(value = "eventImages", required = false) List<MultipartFile> eventImages,
        @Parameter(description = "이벤트 생성 요청 json") @Valid @RequestPart("request") EventCreateRequest request
    ){
        return BaseResponse.onSuccess(
            eventService.createEvent(principal.profileId(), eventImages, request)
        );
    }
//
//    @Operation(summary = "이벤트 수정 API")
//    @PatchMapping(value = "/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public BaseResponse <EventIdResponse> updateEvent (
//            @Parameter(description = "수정할 이벤트 id") @PathVariable Long eventId,
//            @RequestPart(value = "eventImages", required = false) List<MultipartFile> newImages,
//            @Valid @RequestPart("request") EventUpdateRequest request){
//        return BaseResponse.onSuccess(eventService.updateEvent(eventId, newImages, request));
//    }
//
//    @Operation(summary = "이벤트 삭제 API")
//    @DeleteMapping("/{eventId}")
//    public BaseResponse<EventIdResponse> deleteEvent(
//            @Parameter(description = "삭제할 이벤트 id") @PathVariable Long eventId) {
//        return BaseResponse.onSuccess(eventService.deleteEvent(eventId));
//    }
//
//    @Operation(summary = "이벤트 찜하기 API")
//    @PostMapping("/{eventId}/like")
//    public BaseResponse<Boolean> likeEvent(
//            @Parameter(description = "찜할 이벤트 id") @PathVariable("eventId") Long eventId){
//        return BaseResponse.onSuccess(eventService.likeEvent(eventId));
//    }
//
//    @Operation(summary = "이벤트 상세 조회 API")
//    @GetMapping("/{eventId}")
//    public BaseResponse<EventDetailInquiryResponse> inquiryEventByEventId (
//            @Parameter(description = "상세 조회할 이벤트 id") @PathVariable("eventId") Long eventId) {
//        return BaseResponse.onSuccess(eventService.inquiryEventDetailByEventId(eventId));
//    }
//
//    @Operation(summary = "이벤트 검색 API", description = "학교 이름, 이벤트 이름, 세부 설명에서 키워드 검색")
//    @Parameters(value = {
//            @Parameter(name = "keyword", description = "검색할 키워드로 한글자 이상 입력"),
//            @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
//            @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
//    })
//    @GetMapping("/search")
//    public BaseResponse<EventPagingResponse<EventSummaryInquiryResponse>> inquiryEventByName(
//            @RequestParam(name = "keyword") String keyword,
//            @RequestParam(name = "page") int page,
//            @RequestParam(name = "size") int size) {
//        return BaseResponse.onSuccess(eventService.inquiryEventByKeyword(keyword, page, size));
//    }
//
//    @Operation(summary = "이벤트 조회 API(추천순, 거리순, 최근순)")
//    @Parameters(value = {
//            @Parameter(name = "type", description = "조회 타입 지정(추천순: RECOMMEND, 거리순: DISTANCE, 최근순: RECENT"),
//            @Parameter(name = "organizationId", description = "조직 ID (필터링에 사용)"),
//            @Parameter(name = "latitude", description = "거리순일 경우 유저의 현재 위치의 위도(추천순, 최근순의 경우 사용 X)"),
//            @Parameter(name = "longitude", description = "거리순일 경우 유저의 현재 위치의 경도(추천순, 최근순의 경우 사용 X)"),
//            @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
//            @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
//    })
//    @GetMapping
//    public BaseResponse<EventPagingResponse<EventSummaryInquiryResponse>> inquiryEvents(
//            @RequestParam(name = "type") InquiryType type,
//            @RequestParam(name = "organizationId", required = false) Long organizationId,
//            @RequestParam(name = "latitude", required = false) double latitude,
//            @RequestParam(name = "longitude", required = false) double longitude,
//            @RequestParam(name = "page") int page,
//            @RequestParam(name = "size") int size) {
//        return BaseResponse.onSuccess(eventService.inquiryEvents(type, organizationId, latitude, longitude, page, size));
//    }
//
//
//    @Operation(summary = "나의 이벤트 조회 API")
//    @Parameters(value = {
//            @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
//            @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
//    })
//    @GetMapping("/mine")
//    public BaseResponse<EventPagingResponse<EventSummaryInquiryResponse>> inquiryEventByMember(
//            @RequestParam(name = "page") int page,
//            @RequestParam(name = "size") int size) {
//        return BaseResponse.onSuccess(eventService.inquiryEventByMember(page, size));
//    }
//
//    @Operation(summary = "내가 찜한 이벤트 조회 API")
//    @Parameters(value = {
//            @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
//            @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
//    })
//    @GetMapping("/like")
//    public BaseResponse<EventPagingResponse<EventSummaryInquiryResponse>> inquiryEventByLike(
//            @RequestParam(name = "page") int page,
//            @RequestParam(name = "size") int size) {
//        return BaseResponse.onSuccess(eventService.inquiryEventByLike(page, size));
//    }
//
//    @Operation(summary = "내 주변 이벤트 조회 API")
//    @Parameters(value = {
//            @Parameter(name = "latitude", description = "현재 유저 위도 위치입니다. (-90 ~ 90)"),
//            @Parameter(name = "longitude", description = "현재 유저의 경도 위치입니다. (-180 ~ 180)"),
//            @Parameter(name = "radius", description = "조회할 위치 반경입니다. (0보다 큰 정수)"),
//            @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
//            @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
//    })
//    @GetMapping("/location")
//    public BaseResponse<EventPagingResponse<EventLocationInquiryResponse>> inquiryEventByLocation(
//            @RequestParam(value = "latitude") double latitude,
//            @RequestParam(value = "longitude") double longitude,
//            @RequestParam(value = "radius") int radius,
//            @RequestParam(name = "page") int page,
//            @RequestParam(name = "size") int size) {
//        return BaseResponse.onSuccess(eventService.inquiryEventByLocation(latitude, longitude, radius, page, size));
//    }
}
