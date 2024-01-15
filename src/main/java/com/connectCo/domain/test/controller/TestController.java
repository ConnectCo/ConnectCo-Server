package com.connectCo.domain.test.controller;

import com.connectCo.domain.test.dto.request.TestRequest;
import com.connectCo.global.common.BaseResponse;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.utils.S3FileComponent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "예제 API", description = "테스트용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final S3FileComponent s3FileComponent;

    @Operation(summary = "에러 핸들러 테스트 API", description = "직접 정의한 에러에 대한 예외 처리 테스트 API")
    @PostMapping("/customException")
    public BaseResponse<Object> throwCustomException() {
        throw new CustomApiException(ErrorCode.USER_NOT_FOUND);
    }

    @Operation(summary = "에러 핸들러 테스트 API", description = "ConstraintViolationException에 대한 예외 처리 테스트 API")
    @GetMapping("/constraintViolation")
    public BaseResponse<String> throwConstraintViolationException(@RequestParam(value = "name") String name) {

        return BaseResponse.onSuccess("테스트 API");
    }

    @Operation(summary = "에러 핸들러 테스트 API", description = "MethodArgumentNotValidException에 대한 예외 처리 테스트 API")
    @PostMapping("/methodArgumentNotValid")
    public BaseResponse<String> throwMethodArgumentNotValidException(@Valid @RequestBody TestRequest request) {
        return BaseResponse.onSuccess("테스트 API");
    }

    @Operation(summary = "파일 생성 API")
    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<String> createFile(@RequestPart("file") MultipartFile file) {
        return BaseResponse.onSuccess(s3FileComponent.uploadFile("test", file));
    }

    @DeleteMapping("/files")
    @Operation(summary = "파일 삭제 API")
    public void deleteFile(@RequestParam("fileUrl") String fileUrl) {
        s3FileComponent.deleteFile(fileUrl);
    }
}