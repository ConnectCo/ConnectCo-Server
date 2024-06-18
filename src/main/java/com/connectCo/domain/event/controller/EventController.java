package com.connectCo.domain.event.controller;

import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.request.EventUpdateRequest;
import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventIdResponse;
import com.connectCo.domain.event.dto.response.EventLikeResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import com.connectCo.domain.event.service.EventService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "이벤트 API", description = "이벤트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;


    @Operation(summary = "이벤트 생성 API")
    @PostMapping("")
    public BaseResponse<EventIdResponse> createEvent (
            @RequestPart(value = "eventImages", required = false) List<MultipartFile> eventImages,
            @Valid @RequestPart("request") EventCreateRequest request){
        return BaseResponse.onSuccess(eventService.createEvent(eventImages, request));
    }

    @Operation(summary = "이벤트 수정 API")
    @PatchMapping("/{eventId}")
    public BaseResponse <EventIdResponse> updateEvent (
            @Parameter(description = "수정할 이벤트 id") @PathVariable Long eventId,
            @RequestPart(value = "eventImages", required = false) List<MultipartFile> newImages,
            @Valid @RequestPart("request") EventUpdateRequest request){
        return BaseResponse.onSuccess(eventService.updateEvent(eventId, newImages, request));
    }

    @Operation(summary = "이벤트 삭제 API")
    @DeleteMapping("/{eventId}")
    public BaseResponse<EventIdResponse> deleteEvent(
            @Parameter(description = "삭제할 이벤트 id") @PathVariable Long eventId) {
        return BaseResponse.onSuccess(eventService.deleteEvent(eventId));
    }

    @Operation(summary = "이벤트 검색 API", description = "학교 이름, 이벤트 이름, 세부 설명에서 키워드 검색")
    @Parameters(value = {
            @Parameter(name = "keyword", description = "검색할 키워드로 한글자 이상 입력"),
            @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
            @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
    })
    @GetMapping("/search")
    public BaseResponse<List<EventSummaryInquiryResponse>> inquiryEventByName(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        return BaseResponse.onSuccess(eventService.inquiryEventByKeyword(keyword, page, size));
    }

    @Operation(summary = "이벤트 상세 조회 API")
    @GetMapping("/{eventId}")
    public BaseResponse<EventDetailInquiryResponse> inquiryEventByEventId (
            @Parameter(description = "상세 조회할 이벤트 id") @PathVariable("eventId") Long eventId) {
        return BaseResponse.onSuccess(eventService.inquiryEventDetailByEventId(eventId));
    }

    @Operation(summary = "이벤트 조회 API(추천순, 거리순, ")
    @GetMapping("/recent")
    public BaseResponse<List<EventSummaryInquiryResponse>> inquiryEventByCreatedAt(){
        return BaseResponse.onSuccess(eventService.inquiryEventByRecent());
    }

    @Operation(summary = "이벤트 찜하기 API")
    @PostMapping("/{eventId}/like")
    public BaseResponse<EventLikeResponse> likeEvent(@PathVariable("eventId") Long eventId){
        return BaseResponse.onSuccess(eventService.likeEvent(eventId));
    }


    @Operation(summary = "나의 이벤트 조회 API")
    @GetMapping("/mine")
    public BaseResponse<List<EventSummaryInquiryResponse>> inquiryEventByMember() {
        return BaseResponse.onSuccess(eventService.inquiryEventByMember());
    }

    @Operation(summary = "내가 찜한 이벤트 조회 API")
    @GetMapping("/like")
    public BaseResponse<List<EventSummaryInquiryResponse>> inquiryEventByLike() {
        return BaseResponse.onSuccess(eventService.inquiryEventByLike());
    }
}
