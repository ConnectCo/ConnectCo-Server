package com.connectCo.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder( {"isSuccess", "code", "message", "result"} )
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private Boolean isSuccess;
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 성공한 경우 응답 생성

    public static <T> BaseResponse<T> onSuccess(T result){
        return new BaseResponse<>(true, "200" , "요청에 성공하였습니다.", result);
    }

    // 실패한 경우 응답 생성
    public static <T> BaseResponse<T> onFailure(String code, String message, T data){
        return new BaseResponse<>(false, code, message, data);
    }

}
