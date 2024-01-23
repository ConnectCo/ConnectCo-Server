package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.coupon.entity.CouponImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponImageRepository extends JpaRepository<CouponImage, UUID> {

}
