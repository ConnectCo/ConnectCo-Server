package com.connectCo.domain.event.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateRequest {
    @NotBlank(message = "이벤트 이름은 필수 입력값입니다.")
    private String name;
    @NotBlank(message = "이벤트 주소는 필수 입력값입니다.")
    private String detailAddress;
    private double latitude;
    private double longitude;
    // 조직 미선택시 null값
    private Long organizationId;
    private LocalDate startAt;
    @Future(message = "이벤트 협찬 종료일의 경우, 과거 날짜를 선택할 수 없습니다.")
    private LocalDate endAt;
    @Future(message = "이벤트 협찬 제안 마감일의 경우, 과거 날짜를 선택할 수 없습니다.")
    private LocalDate expiredAt;
    private String benefitTarget;
    private String description;
    private String priorityTarget;
    private String notification;
    @NotNull(message = "기존 이미지가 모두 삭제된 경우, 빈 리스트 형태로 보내주세요.")
    private List<String> existingImages;
}


