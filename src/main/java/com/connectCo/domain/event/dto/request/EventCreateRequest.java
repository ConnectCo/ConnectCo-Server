package com.connectCo.domain.event.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateRequest {
    private String organizationName;
    private String address;
    private String name;
    private String description;
    private String priorityTarget;
    private String benefitTarget;
    private String notification;
    private LocalDate expiredAt;
    private String thumbnail;
}


