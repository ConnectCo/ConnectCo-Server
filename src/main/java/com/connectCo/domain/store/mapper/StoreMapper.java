package com.connectCo.domain.store.mapper;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.response.StoreSummaryInquiryResponse;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public Store toStore(Member member, StoreCreateRequest request, Address address) {
        return Store.builder()
                .name(request.getName())
                .address(address)
                .storeNumber(request.getStoreNumber())
                .operatingTime(request.getOperatingTime())
                .description(request.getDescription())
                .couponCount(0)
                .member(member)
                .build();
    }

    public StoreImage toStoreImage(Store store, String url) {
        return StoreImage.builder()
                .store(store)
                .url(url)
                .build();
    }

    public StoreSummaryInquiryResponse toStoreSummaryInquiryResponse(Store store) {
        String thumbnail = store.getImages().stream()
                .findFirst()
                .map(StoreImage::getUrl)
                .orElse(null);

        return StoreSummaryInquiryResponse.builder()
                .storeId(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .thumbnail(thumbnail)
                .couponCount(store.getCouponCount())
                .build();
    }
}
