package com.connectCo.domain.address.service;

import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address createAddress(String detailAddress, double latitude, double longitude) {
        return addressRepository.save(
                Address.builder()
                        .detailAddress(detailAddress)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build());
    }

}
