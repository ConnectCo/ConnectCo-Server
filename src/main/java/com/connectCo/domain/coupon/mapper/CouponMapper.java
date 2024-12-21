package com.connectCo.domain.coupon.mapper;

import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailResponse;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.entity.CouponImage;
import com.connectCo.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CouponMapper {

    public Coupon toCoupon(Store store, CouponCreateRequest request){
        return Coupon.builder()
                .name(request.getName())
                .expiredAt(LocalDate.parse(request.getExpiredAt()))
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


    public CouponSummaryInquiryResponse toCouponSummaryInquiryResponse(Coupon coupon) {
        String thumbnail = coupon.getImages().stream()
                .findFirst()
                .map(CouponImage::getUrl)
                .orElse(null);

        return CouponSummaryInquiryResponse.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .description(coupon.getDescription())
                .expiredAt(coupon.getExpiredAt())
                .thumbnail(thumbnail)
                .build();
    }

    public CouponDetailResponse toCouponDetailResponse(Coupon coupon) {
        List<String> imageUrls = coupon.getImages().stream()
                .map(CouponImage::getUrl)
                .collect(Collectors.toList());


        return CouponDetailResponse.builder()
                .id(coupon.getId())
                .storeId(coupon.getStore().getId())
                .storeName(coupon.getStore().getName())
                .name(coupon.getName())
                .description(coupon.getDescription())
                .priorityTarget(coupon.getPriorityTarget())
                .notification(coupon.getNotification())
                .expiredAt(coupon.getExpiredAt())
                .images(imageUrls)
                .build();
    }

    public <T> CouponPagingResponse<T> toCouponPagingResponse(Page<T> coupons){
        return CouponPagingResponse.<T>builder()
                .coupons(coupons.getContent())
                .totalPages(coupons.getTotalPages())
                .totalElements((int) coupons.getTotalElements())
                .isFirst(coupons.isFirst())
                .isLast(coupons.isLast())
                .build();
    }
}
