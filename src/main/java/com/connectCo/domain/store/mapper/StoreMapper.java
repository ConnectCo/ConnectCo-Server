package com.connectCo.domain.store.mapper;

import com.connectCo.domain.Member.dto.response.MemberInfoResponse;
import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreMapper {

    public Store toStore(Member member, StoreCreateRequest request) {
        return Store.builder()
                .name(request.getName())
                .address(request.getAddress())
                .storeNumber(request.getStoreNumber())
                .operatingTime(request.getOperatingTime())
                .description(request.getDescription())
                .member(member)
                .build();
    }

    public StoreImage toStoreImage(Store store, String url) {
        return StoreImage.builder()
                .store(store)
                .url(url)
                .build();
    }
}
