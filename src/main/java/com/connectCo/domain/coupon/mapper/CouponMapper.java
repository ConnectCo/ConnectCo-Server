package com.connectCo.domain.coupon.mapper;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailInquiryResponse;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.entity.CouponImage;
import com.connectCo.domain.coupon.entity.CouponLike;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public Coupon toCoupon(Store store, CouponCreateRequest request){
        return Coupon.builder()
                .name(request.getName())
                .expiredAt(request.getExpiredAt())
                .description(request.getDescription())
                .priorityTarget(request.getPriorityTarget())
                .notification(request.getNotification())
                .store(store)
                .build();

    }

    public CouponImage toCouponImage(Coupon coupon, String url) {
        return CouponImage.builder()
                .coupon(coupon)
                .url(url)
                .build();
    }

    public CouponLike toCouponLike(Coupon coupon, Organization organization) {
        return CouponLike.builder()
                .coupon(coupon)
                .organization(organization)
                .isActive(true)
                .build();
    }

    public <T>CouponPagingResponse<T> toCouponPagingResponse(Page<T> coupons) {
        return CouponPagingResponse.<T>builder()
            .coupons(coupons.getContent())
            .page(coupons.getNumber())
            .totalPages(coupons.getTotalPages())
            .totalElements((int) coupons.getTotalElements())
            .isFirst(coupons.isFirst())
            .isLast(coupons.isLast())
            .build();
    }

    public CouponSummaryInquiryResponse toCouponSummaryInquiryResponse(Coupon coupon) {
        String thumbnail = coupon.getImages().stream()
                .findFirst()
                .map(CouponImage::getUrl)
                .orElse(null);

        return CouponSummaryInquiryResponse.builder()
                .couponId(coupon.getId())
                .storeName(coupon.getStore().getName())
                .name(coupon.getName())
                .expiredAt(coupon.getExpiredAt())
                .thumbnail(thumbnail)
                .build();
    }

    public CouponDetailInquiryResponse toCouponDetailResponse(
        Coupon coupon, Boolean isLiked, Boolean isMine
    ) {
        return CouponDetailInquiryResponse.builder()
            .id(coupon.getId())
            .store(toStoreInfo(coupon.getStore()))
            .name(coupon.getName())
            .description(coupon.getDescription())
            .priorityTarget(coupon.getPriorityTarget())
            .notification(coupon.getNotification())
            .expiredAt(coupon.getExpiredAt())
            .createdAt(coupon.getCreatedAt().toLocalDate())
            .images(coupon.getImages().stream()
                .map(CouponImage::getUrl)
                .toList()
            )
            .isLike(isLiked)
            .isMine(isMine)
            .build();
    }

    private CouponDetailInquiryResponse.StoreInfo toStoreInfo(Store store) {
        return new CouponDetailInquiryResponse.StoreInfo(store.getId(), store.getName());
    }
}
