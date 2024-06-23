package com.connectCo.domain.store.dto.request;

import com.connectCo.global.validation.annotation.ExistStore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreCreateRequest {
    @Schema(description = "가게 이름", example = "가게명")
    @NotBlank(message = "가게 이름은 필수 입력값입니다.")
    @ExistStore
    private String name;

    @Schema(description = "가게 주소", example = "주소")
    @NotBlank(message = "가게 주소는 필수 입력값입니다.")
    private String detailAddress;

    @Schema(description = "위도", example = "37.5665")
    private double latitude;

    @Schema(description = "경도", example = "126.9780")
    private double longitude;

    @Schema(description = "가게 번호", example = "010-1234-5678")
    private String storeNumber;

    @Schema(description = "운영 시간", example = "09:00 - 18:00")
    private String operatingTime;

    @Schema(description = "설명", example = "가게 설명")
    private String description;
}
