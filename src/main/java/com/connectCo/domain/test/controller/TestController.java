package com.connectCo.domain.test.controller;

import com.connectCo.domain.test.dto.request.TestRequest;
import com.connectCo.global.common.BaseResponse;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "예제 API", description = "테스트용 API")
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "에러 핸들러 테스트 API", description = "직접 정의한 에러에 대한 예외 처리 테스트 API")
    @PostMapping("/customException")
    public BaseResponse<Object> throwCustomException() {
        throw new CustomApiException(ErrorCode.USER_NOT_FOUND);
    }

    @Operation(summary = "에러 핸들러 테스트 API", description = "ConstraintViolationException 및 MethodArgumentNotValidException에 대한 예외 처리 테스트 API")
    @PostMapping("/methodArgumentNotValid")
    public BaseResponse<String> throwMethodArgumentNotValidException(@Valid @RequestBody TestRequest request) {
        return BaseResponse.onSuccess("테스트 API");
    }

    // 파일 업로드/삭제 테스트 API(/test/files)는 제거했다.
    // /test/**가 비로그인 허용이라 누구나 버킷에 파일을 올리거나 URL만 알면 다른 사용자의 이미지를 지울 수 있었다.
    // 파일 업로드·삭제는 각 도메인 API(이벤트·쿠폰·가게·단체 이미지)에서 권한 확인 후에만 수행된다.
}