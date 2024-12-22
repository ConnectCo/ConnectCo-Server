package com.connectCo.domain.coupon.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.coupon.entity.CouponLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponLikeRepository extends JpaRepository<CouponLike, Long> {
    //List<CouponLike> findAllByMemberAndIsChecked(Member member, boolean isChecked);

    @Query("SELECT cl.coupon FROM CouponLike cl WHERE cl.member = :member AND cl.isChecked = :isChecked")
    Page<CouponLike> findAllByMemberAndIsChecked(@Param("member") Member member, @Param("isChecked") boolean isChecked, Pageable pageable);
}
