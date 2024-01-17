package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.coupon.entity.CouponLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CouponLikeRepository extends JpaRepository<CouponLike, UUID> {
    List<CouponLike> findAllByMemberAndIsChecked(Member member, boolean isChecked);
}
