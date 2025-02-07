package com.connectCo.domain.store.mapper;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.address.entity.Address;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public <T> StorePagingResponse<T> toStorePagingResponse(Page<T> stores) {
        return StorePagingResponse.<T>builder()
                .stores(stores.getContent())
                .page(stores.getNumber())
                .totalPages(stores.getTotalPages())
                .totalElements((int) stores.getTotalElements())
                .isFirst(stores.isFirst())
                .isLast(stores.isLast())
                .build();
    }

    public StoreSummaryInquiryResponse toStoreSummaryInquiryResponse(Store store) {
        // 유효한 쿠폰 개수
        long validCouponCount = store.getCoupons().stream()
            .filter(coupon ->
                coupon.getExpiredAt().isAfter(LocalDate.now()) ||
                coupon.getExpiredAt().isEqual(LocalDate.now())
            ).count();

        return StoreSummaryInquiryResponse.builder()
                .storeId(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .thumbnail(store.getProfileImage())
                .couponCount(validCouponCount)
                .build();
    }

    public StoreDetailInquiryResponse toStoreDetailInquiryResponse(
            Store store, List<String> images, Boolean isLike,
            Boolean isMine, List<StoreDetailInquiryResponse.StoreCoupon> coupons
    ) {
        return StoreDetailInquiryResponse.builder()
                .storeId(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .address(CommonMapper.toAddressResponse(store.getAddress()))
                .phoneNumber(store.getPhoneNumber())
                .operatingTime(store.getOperatingTime())
                .images(images)
                .coupons(coupons)
                .appliedEventCount(store.getAppliedEventCount())
                .isLike(isLike)
                .isMine(isMine)
                .build();
    }

    public StoreDetailInquiryResponse.StoreCoupon toStoreCoupon(Coupon coupon) {
        String couponThumbnail = (coupon.getImages() != null && !coupon.getImages().isEmpty())
            ? coupon.getImages().get(0).getUrl()
            : null;

        return StoreDetailInquiryResponse.StoreCoupon.builder()
            .couponId(coupon.getId())
            .name(coupon.getName())
            .expiredAt(coupon.getExpiredAt())
            .couponThumbnail(couponThumbnail)
            .build();
    }
}
