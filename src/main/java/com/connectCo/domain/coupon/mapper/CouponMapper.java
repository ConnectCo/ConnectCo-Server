package com.connectCo.domain.coupon.mapper;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.entity.CouponImage;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
}
