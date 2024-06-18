package com.connectCo.domain.event.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateRequest {
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    // 조직 미선택시 null값
    private Long organizationId;
    private LocalDate startAt;
    private LocalDate endAt;
    private LocalDate expiredAt;
    private String benefitTarget;
    private String description;
    private String priorityTarget;
    private String notification;
}


