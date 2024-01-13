package com.connectCo.domain.test.controller;

import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예제 API", description = "테스트용 API")
@RestController
@RequestMapping("/test")
public class TestController {
    @Operation(summary = "예시 API")
    @GetMapping("/example")
    public BaseResponse<String> example() {
        return BaseResponse.onSuccess( "예시 API");
    }
}