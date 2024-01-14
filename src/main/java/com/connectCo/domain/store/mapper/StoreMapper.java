package com.connectCo.domain.store.mapper;

import com.connectCo.domain.Member.dto.response.MemberInfoResponse;
import com.connectCo.domain.store.entity.Store;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreMapper {

    public MemberInfoResponse.MyStores toMyStores(Store store) {
        return MemberInfoResponse.MyStores.builder()
                .storeId(store.getId())
                .name(store.getName())
                .build();
    }
}
