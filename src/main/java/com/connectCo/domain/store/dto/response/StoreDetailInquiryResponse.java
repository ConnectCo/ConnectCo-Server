package com.connectCo.domain.store.dto.response;

import com.connectCo.domain.address.entity.Address;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class StoreDetailInquiryResponse {
    private Long storeId;
    private String name;
    private String description;
    private Address address;
    private String number;
    private String operatingTime;
    private List<String> images;
    private List<StoreCoupon> coupons;

    @Getter
    @Builder
    public static class StoreCoupon {
        private Long couponId;
        private String name;
        private LocalDate expiredAt;
    }
}
