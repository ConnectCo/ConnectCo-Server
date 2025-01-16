package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.coupon.entity.CouponLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponLikeRepository extends JpaRepository<CouponLike, Long> {
    List<CouponLike> findAllByMemberAndIsChecked(Member member, boolean isChecked);
}
