package com.connectCo.global.exception;

import com.connectCo.global.common.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class CustomRestControllerAdvice extends ResponseEntityExceptionHandler {

    /*
     * 직접 정의한 에러에 대한 예외 처리
     */
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> handleRestApiResponse(CustomApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

}
