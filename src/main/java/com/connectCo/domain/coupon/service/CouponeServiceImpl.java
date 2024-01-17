package com.connectCo.domain.coupon.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.entity.CouponLike;
import com.connectCo.domain.coupon.mapper.CouponMapper;
import com.connectCo.domain.coupon.repository.CouponLikeRepository;
import com.connectCo.domain.coupon.repository.CouponRepository;
import com.connectCo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponeServiceImpl implements CouponService {

    private final StoreService storeService;
    private final AuthService authService;
    private final CouponRepository couponRepository;
    private final CouponLikeRepository couponLikeRepository;
    private final CouponMapper couponMapper;

    @Override
    public List<CouponSummaryInquiryResponse> inquiryCouponByMember() {
        Member member = authService.getLoginMember();

        List<Coupon> couponList = storeService.getStoresByMember(member).stream()
                .flatMap(store -> couponRepository.findAllByStore(store).stream())
                .toList();

        return couponList.stream()
                .map(couponMapper::toCouponSummaryInquiryResponse)
                .toList();
    }

    @Override
    public List<CouponSummaryInquiryResponse> inquiryCouponByLike() {
        Member member = authService.getLoginMember();

        List<Coupon> couponList = couponLikeRepository.findAllByMemberAndIsChecked(member, true).stream()
                .map(CouponLike::getCoupon)
                .toList();

        return couponList.stream()
                .map(couponMapper::toCouponSummaryInquiryResponse)
                .toList();
    }
}
