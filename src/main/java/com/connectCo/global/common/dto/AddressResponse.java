package com.connectCo.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse {
    private String detailAddress;
    private double latitude;
    private double longitude;
}
