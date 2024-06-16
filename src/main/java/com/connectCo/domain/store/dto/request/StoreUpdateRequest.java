package com.connectCo.domain.store.dto.request;

import com.connectCo.global.validation.annotation.ExistStore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreUpdateRequest {
    @NotBlank(message = "가게 이름은 필수 입력값입니다.")
    @ExistStore
    private String name;
    @NotBlank(message = "가게 주소는 필수 입력값입니다.")
    private String detailAddress;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    private String storeNumber;
    private String operatingTime;
    private String description;
    private List<String> existingImages;
}
