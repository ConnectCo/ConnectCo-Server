package com.connectCo.domain.store.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreLikeRepository extends JpaRepository<StoreLike, Long> {

    List<StoreLike> findAllByMemberAndIsChecked(Member member, boolean isChecked);
    Optional<StoreLike> findByMemberAndStore(Member member, Store store);
}
