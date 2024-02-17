package com.connectCo.domain.event.controller;

import com.connectCo.domain.event.dto.request.EventCreateRequest;
import com.connectCo.domain.event.dto.response.EventDetailInquiryResponse;
import com.connectCo.domain.event.dto.response.EventIdResponse;
import com.connectCo.domain.event.dto.response.EventLikeResponse;
import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import com.connectCo.domain.event.service.EventService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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
            @RequestPart EventCreateRequest request){
        return BaseResponse.onSuccess(eventService.createEvent(eventImages, request));
    }

    @Operation(summary = "이벤트 수정 API")
    @PutMapping("/{eventId}")
    public BaseResponse <EventSummaryInquiryResponse> updateEvent (
            @PathVariable UUID eventId,
            @RequestPart EventCreateRequest request,
            @RequestPart(value = "eventImages", required = false) List<MultipartFile> eventImages){
        return BaseResponse.onSuccess(eventService.updateEvent(eventId, request, eventImages));
    }

    @Operation(summary = "이벤트 삭제 API")
    @DeleteMapping("/{eventId}")
    public BaseResponse<UUID> deleteEvent(@PathVariable UUID eventId){
        return BaseResponse.onSuccess(eventService.deleteEvent(eventId));
    }

    @Operation(summary = "이벤트 검색 API")
    @GetMapping("")
    public BaseResponse<List<EventSummaryInquiryResponse>> inquiryEventByName(@RequestParam String keyword) {
        return BaseResponse.onSuccess(eventService.inquiryEventByKeyword(keyword));
    }

    @Operation(summary = "이벤트 세부사항 조회 API")
    @GetMapping("/{eventId}")
    public BaseResponse<EventDetailInquiryResponse> inquiryEventByEventId (@PathVariable("eventId") UUID eventId) {
        return BaseResponse.onSuccess(eventService.inquiryEventDetailByEventId(eventId));
    }

    @Operation(summary = "이벤트 최신순 조회 API")
    @GetMapping("/recent")
    public BaseResponse<List<EventSummaryInquiryResponse>> inquiryEventByCreatedAt(){
        return BaseResponse.onSuccess(eventService.inquiryEventByRecent());
    }

    @Operation(summary = "이벤트 찜하기 API")
    @PostMapping("/{eventId}/like")
    public BaseResponse<EventLikeResponse> likeEvent(@PathVariable("eventId") UUID eventId){
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
