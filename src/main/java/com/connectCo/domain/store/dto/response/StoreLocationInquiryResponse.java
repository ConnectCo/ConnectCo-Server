package com.connectCo.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreLocationInquiryResponse {
    private Long storeId;
    private String name;
    private String description;
    private String thumbnail;
    private int couponCount;
    private double latitude;
    private double longitude;
    private double distance;
}
