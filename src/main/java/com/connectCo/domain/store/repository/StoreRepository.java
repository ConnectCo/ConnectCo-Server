package com.connectCo.domain.store.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    default Store getStore(Long storeId) {
        return findById(storeId)
            .orElseThrow(() -> new CustomApiException(ErrorCode.STORE_NOT_FOUND));
    }

    boolean existsByName(String name);
    boolean existsStoreByName(String name);
    long countByMember(Member member);
}
