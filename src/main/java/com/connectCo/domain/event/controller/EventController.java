package com.connectCo.domain.event.controller;

import com.connectCo.domain.event.dto.response.EventSummaryInquiryResponse;
import com.connectCo.domain.event.service.EventService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "이벤트 API", description = "이벤트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "이벤트 검색 API")
    @GetMapping("/?name=keyword")
    public BaseResponse<List<EventSummaryInquiryResponse>> inquiryEventByName(@RequestParam String name) {
        return BaseResponse.onSuccess(eventService.inquiryEventByName(name));
    }
    @Operation(summary = "이벤트 세부사항 조회 API")
    @GetMapping("/{event-id}")
    public BaseResponse<List<EventSummaryInquiryResponse>> inquiryEventByEventId (@PathVariable("event-id") String eventId) {
        return BaseResponse.onSuccess(eventService.inquiryEventByEventId(eventId));
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
