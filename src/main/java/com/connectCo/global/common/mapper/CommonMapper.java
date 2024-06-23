package com.connectCo.global.common.mapper;

import com.connectCo.domain.address.entity.Address;
import com.connectCo.global.common.dto.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class CommonMapper {

    public static AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .detailAddress(address.getDetailAddress())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
    }
}
