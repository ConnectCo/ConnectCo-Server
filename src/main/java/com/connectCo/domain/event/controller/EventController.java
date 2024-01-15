package com.connectCo.domain.event.controller;

import com.connectCo.domain.event.dto.response.EventInquiryByMemberResponse;
import com.connectCo.domain.event.service.EventService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "이벤트 API", description = "이벤트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "나의 이벤트 조회 API")
    @GetMapping("/member")
    public BaseResponse<List<EventInquiryByMemberResponse>> inquiryEventByMember() {
        return BaseResponse.onSuccess(eventService.inquiryEventByMember());
    }
}
