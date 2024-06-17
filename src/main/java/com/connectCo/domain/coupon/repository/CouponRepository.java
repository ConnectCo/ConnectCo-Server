package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findAllByStore(Store store);

    Page<Coupon> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
