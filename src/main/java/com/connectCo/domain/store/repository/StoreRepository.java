package com.connectCo.domain.store.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByName(String name);
    boolean existsStoreByName(String name);
    long countByMember(Member member);
}
