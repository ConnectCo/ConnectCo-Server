package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    default Coupon getCoupon(Long couponId) {
        return findById(couponId)
            .orElseThrow(() -> new CustomApiException(ErrorCode.COUPON_NOT_FOUND));
    }


    List<Coupon> findAllByStore(Store store);

    Page<Coupon> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
