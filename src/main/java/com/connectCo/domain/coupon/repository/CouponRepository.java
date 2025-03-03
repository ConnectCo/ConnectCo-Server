package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    default Coupon getCoupon(Long couponId) {
        return findById(couponId)
            .orElseThrow(() -> new CustomApiException(ErrorCode.COUPON_NOT_FOUND));
    }
    Page<Coupon> findAllByStore(Store store, Pageable pageable);
    Page<Coupon> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Coupon> findAllByOrderByExpiredAtAsc(Pageable pageable);
    @Query(value = """
        SELECT c.* FROM coupon c
        JOIN store s ON c.store_id = s.id
        JOIN address a ON s.address_id = a.id
        ORDER BY ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(a.longitude, a.latitude))
    """, nativeQuery = true)
    Page<Coupon> findByDistance(
        @Param("latitude") double latitude, @Param("longitude") double longitude, Pageable pageable
    );
}
