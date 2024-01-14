package com.connectCo.domain.store.dto.request;

import com.connectCo.global.validation.annotation.ExistStore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StoreCreateRequest {
    @NotBlank(message = "가게 이름은 필수 입력값입니다.")
    @ExistStore
    private String name;
    @NotBlank(message = "가게 주소는 필수 입력값입니다.")
    private String address;
    private String storeNumber;
    private String operatingTime;
    private String description;
}
