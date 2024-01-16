package com.connectCo.domain.store.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.store.entity.StoreLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreLikeRepository extends JpaRepository<StoreLike, UUID> {

    List<StoreLike> findAllByMemberAndIsChecked(Member member, boolean isChecked);
}
