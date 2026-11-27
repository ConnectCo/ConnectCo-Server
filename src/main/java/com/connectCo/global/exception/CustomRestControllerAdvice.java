package com.connectCo.global.exception;

import com.connectCo.global.common.BaseResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

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
                .map(ConstraintViolation::getMessage)
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

        String errorMessage =  Optional.ofNullable(e.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage()).orElse("");

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorMessage, null));
    }

    /*
     * 유니크 제약 위반(예: 좋아요를 동시에 두 번 눌러 같은 행을 두 번 넣으려 한 경우)
     * 500 대신 409로 응답하고, 클라이언트는 다시 시도하면 기존 행을 기준으로 처리된다.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.warn("데이터 무결성 위반 cause={}", e.getMostSpecificCause().getClass().getSimpleName());
        ErrorCode errorCode = ErrorCode.DUPLICATE_REQUEST;
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
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
