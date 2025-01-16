package com.connectCo.domain.store.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreLikeRepository extends JpaRepository<StoreLike, Long> {

    Page<StoreLike> findAllByMemberAndIsChecked(Member member, boolean isChecked, Pageable pageable);
    Optional<StoreLike> findByMemberAndStore(Member member, Store store);
}
