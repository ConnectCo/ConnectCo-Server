package com.connectCo.global.exception;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {
    private ErrorCode errorCode;
}
