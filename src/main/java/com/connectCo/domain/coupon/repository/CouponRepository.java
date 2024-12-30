package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    //마이페이지 가게별 내 쿠폰 조회
    @Query("SELECT c FROM Coupon c WHERE c.store IN :stores AND c.expiredAt >= :currentDate ORDER BY c.createdAt DESC ")
    Page<Coupon> findAllByStores(@Param("stores") List<Store> stores, @Param("currentDate") LocalDate currentDate, Pageable pageable);

    // 추천순으로 쿠폰 조회
    @Query("SELECT c FROM Coupon c JOIN c.store.address a WHERE c.expiredAt >= :currentDate " +
            "ORDER BY function('ST_Distance_Sphere', point(a.longitude, a.latitude), point(:longitude, :latitude)) ASC")
    Page<Coupon> findAllByRecommend(@Param("latitude") double latitude,
                                    @Param("longitude") double longitude,
                                    @Param("currentDate") LocalDate currentDate,
                                    Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.expiredAt >= :currentDate  ORDER BY c.createdAt DESC")
    Page<Coupon> findAllByCreatedAt(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT c FROM Coupon c JOIN c.address a WHERE c.expiredAt >= :currentDate " +
            "ORDER BY function('ST_Distance_Sphere', point(a.longitude, a.latitude), point(:longitude, :latitude)) ASC")
    Page<Coupon> findAllByDistance(@Param("latitude") double latitude, @Param("longitude") double longitude,
                                   @Param("currentDate") LocalDate currentDate, Pageable pageable);
}
