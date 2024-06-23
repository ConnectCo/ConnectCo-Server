package com.connectCo.domain.store.dto.request;

import com.connectCo.global.validation.annotation.ExistStore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "가게 수정 요청 json")
public class StoreUpdateRequest {
    @Schema(description = "가게 이름", example = "가게명")
    @NotBlank(message = "가게 이름은 필수 입력값입니다.")
    @ExistStore
    private String name;

    @Schema(description = "가게 주소", example = "주소")
    @NotBlank(message = "가게 주소는 필수 입력값입니다.")
    private String detailAddress;

    @Schema(description = "위도", example = "37.5665")
    @NotNull
    private double latitude;

    @Schema(description = "경도", example = "126.9780")
    @NotNull
    private double longitude;

    @Schema(description = "가게 번호", example = "010-1234-5678")
    private String storeNumber;

    @Schema(description = "운영 시간", example = "09:00 - 18:00")
    private String operatingTime;

    @Schema(description = "설명", example = "가게 설명")
    private String description;

    @Schema(description = "기존 이미지 URL 목록", example = "[\"s3 url1\", \"s3 url2\"]")
    private List<String> existingImages;
}