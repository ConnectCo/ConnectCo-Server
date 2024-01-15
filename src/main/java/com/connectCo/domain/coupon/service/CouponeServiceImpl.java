package com.connectCo.domain.coupon.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.coupon.dto.response.CouponInquiryByMemberResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.mapper.CouponMapper;
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
    private final CouponMapper couponMapper;

    @Override
    public List<CouponInquiryByMemberResponse> inquiryCouponByMember() {
        Member member = authService.getLoginMember();

        List<Coupon> couponList = storeService.getStoresByMember(member).stream()
                .flatMap(store -> couponRepository.findAllByStore(store).stream())
                .toList();

        return couponList.stream()
                .map(couponMapper::toCouponInquiryByMemberResponse)
                .toList();
    }
}
