package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c WHERE c.store IN :stores AND c.expiredAt >= :currentDate ORDER BY c.createdAt DESC ")
    Page<Coupon> findAllByStores(@Param("stores") List<Store> stores, @Param("currentDate") LocalDate currentDate, Pageable pageable);

    Page<Coupon> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
