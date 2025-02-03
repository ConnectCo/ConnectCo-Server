package com.connectCo.domain.store.mapper;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.response.StoreDetailInquiryResponse;
import com.connectCo.domain.store.dto.response.StoreLocationInquiryResponse;
import com.connectCo.domain.store.dto.response.StorePagingResponse;
import com.connectCo.domain.store.dto.response.StoreSummaryInquiryResponse;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import com.connectCo.domain.store.entity.StoreLike;
import com.connectCo.global.common.mapper.CommonMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreMapper {

    public Store toStore(Member member, StoreCreateRequest request, Address address) {
        return Store.builder()
            .name(request.getName())
            .description(request.getDescription())
            .phoneNumber(request.getStoreNumber())
            .operatingTime(request.getOperatingTime())
            .address(address)
            .member(member)
            .profileType(ProfileType.STORE)
            .build();
    }

    public StoreImage toStoreImage(Store store, String url) {
        return StoreImage.builder()
                .store(store)
                .url(url)
                .build();
    }

    public StoreLike toStoreLike(Organization organization, Store store) {
        return StoreLike.builder()
                .store(store)
                .organization(organization)
                .isActive(true)
                .build();
    }
//
//    public <T>StorePagingResponse<T> toStorePagingResponse(Page<T> stores) {
//        return StorePagingResponse.<T>builder()
//                .stores(stores.getContent())
//                .page(stores.getNumber())
//                .totalPages(stores.getTotalPages())
//                .totalElements((int) stores.getTotalElements())
//                .isFirst(stores.isFirst())
//                .isLast(stores.isLast())
//                .build();
//    }
//
//    public StoreSummaryInquiryResponse toStoreSummaryInquiryResponse(Store store) {
//
//        return StoreSummaryInquiryResponse.builder()
//                .storeId(store.getId())
//                .name(store.getName())
//                .description(store.getDescription())
//                .thumbnail(store.getThumbnail())
//                .couponCount(store.getCouponCount())
//                .build();
//    }
//
//    public StoreDetailInquiryResponse toStoreDetailInquiryResponse(
//            Store store, List<String> images, List<StoreDetailInquiryResponse.StoreCoupon> coupons) {
//        return StoreDetailInquiryResponse.builder()
//                .storeId(store.getId())
//                .name(store.getName())
//                .description(store.getDescription())
//                .address(CommonMapper.toAddressResponse(store.getAddress()))
//                .number(store.getStoreNumber())
//                .operatingTime(store.getOperatingTime())
//                .images(images)
//                .coupons(coupons)
//                .build();
//    }
//
//    public StoreLocationInquiryResponse toStoreLocationInquiryResponse(Object[] storeWithDistance) {
//        Store store = (Store) storeWithDistance[0];
//        double distance = (double) storeWithDistance[1];
//        return StoreLocationInquiryResponse.builder()
//                .storeId(store.getId())
//                .name(store.getName())
//                .description(store.getDescription())
//                .thumbnail(store.getThumbnail())
//                .latitude(store.getAddress().getLatitude())
//                .longitude(store.getAddress().getLongitude())
//                .couponCount(store.getCouponCount())
//                .distance(distance)
//                .build();
//    }
//
//    public StoreDetailInquiryResponse.StoreCoupon toStoreCoupon(Coupon coupon) {
//        return StoreDetailInquiryResponse.StoreCoupon.builder()
//                .couponId(coupon.getId())
//                .name(coupon.getName())
//                .expiredAt(coupon.getExpiredAt())
//                .build();
//    }
}
