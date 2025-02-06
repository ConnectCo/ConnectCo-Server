package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.coupon.entity.CouponLike;
import com.connectCo.domain.organization.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponLikeRepository extends JpaRepository<CouponLike, Long> {
    Page<CouponLike> findAllByOrganizationAndIsActiveTrue(Organization organization, Pageable pageable);
}
