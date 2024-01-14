package com.connectCo.global.exception;

import com.connectCo.global.common.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class CustomRestControllerAdvice extends ResponseEntityExceptionHandler {

    /*
     * 직접 정의한 에러에 대한 예외 처리
     */
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<Object> handleRestApiResponse(CustomApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    /*
     * ConstraintViolationException에 대한 예외 처리
     * - Bean Validation을 위반한 경우
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        String errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorMessage, null));
    }

    /*
     * MethodArgumentNotValidException에 대한 예외 처리
     * - RequestBody 내부 유효성 검사 실패 에러
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                               HttpStatusCode status, WebRequest request) {

        ErrorCode errorCode = ErrorCode.BAD_REQUEST;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), e.getMessage(), null));
    }

    /*
     * 일반적인 서버 에러에 대한 예외 처리
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception e) {
        e.printStackTrace(); //예외 정보 출력

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }
}
