package com.connectCo.domain.store.mapper;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.response.StoreDetailInquiryResponse;
import com.connectCo.domain.store.dto.response.StoreLocationInquiryResponse;
import com.connectCo.domain.store.dto.response.StoreSummaryInquiryResponse;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import org.springframework.stereotype.Component;

import java.util.List;

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

        return StoreSummaryInquiryResponse.builder()
                .storeId(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .thumbnail(store.getThumbnail())
                .couponCount(store.getCouponCount())
                .build();
    }

    public StoreDetailInquiryResponse toStoreDetailInquiryResponse(
            Store store, List<String> images, List<StoreDetailInquiryResponse.StoreCoupon> coupons) {
        return StoreDetailInquiryResponse.builder()
                .storeId(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .address(store.getAddress())
                .number(store.getStoreNumber())
                .operatingTime(store.getOperatingTime())
                .images(images)
                .coupons(coupons)
                .build();
    }

    public StoreLocationInquiryResponse toStoreLocationInquiryResponse(Store store, double distance) {

        return StoreLocationInquiryResponse.builder()
                .storeId(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .thumbnail(store.getThumbnail())
                .latitude(store.getAddress().getLatitude())
                .longitude(store.getAddress().getLongitude())
                .couponCount(store.getCouponCount())
                .distance(distance)
                .build();
    }

    public StoreDetailInquiryResponse.StoreCoupon toStoreCoupon(Coupon coupon) {
        return StoreDetailInquiryResponse.StoreCoupon.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .expiredAt(coupon.getExpiredAt())
                .build();
    }
}
