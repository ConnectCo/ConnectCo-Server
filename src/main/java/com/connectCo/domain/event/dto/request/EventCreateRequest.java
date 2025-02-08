package com.connectCo.domain.event.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateRequest {
    @Schema(description = "이벤트 이름", example = "이벤트 A")
    @NotBlank(message = "이벤트 이름은 필수 입력값입니다.")
    private String name;

    @Schema(description = "이벤트 주소", example = "주소")
    @NotBlank(message = "이벤트 주소는 필수 입력값입니다.")
    private String detailAddress;

    @Schema(description = "위도", example = "37.5665")
    private double latitude;

    @Schema(description = "경도", example = "126.9780")
    private double longitude;

    @Schema(description = "이벤트 협찬 시작일", example = "2021-10-01")
    private LocalDate startAt;

    @Schema(description = "이벤트 협찬 종료일", example = "2021-10-31")
    @Future(message = "이벤트 협찬 종료일의 경우, 과거 날짜를 선택할 수 없습니다.")
    private LocalDate endAt;

    @Schema(description = "이벤트 협찬 제안 마감일", example = "2021-09-30")
    @Future(message = "이벤트 협찬 제안 마감일의 경우, 과거 날짜를 선택할 수 없습니다.")
    private LocalDate expiredAt;

    @Schema(description = "혜택 대상", example = "가게 A")
    private String benefitTarget;

    @Schema(description = "이벤트 설명", example = "이벤트 A 설명")
    private String description;

    @Schema(description = "우선협상 대상", example = "커피")
    private String priorityTarget;

    @Schema(description = "유의사항", example = "유의사항")
    private String notification;
}


