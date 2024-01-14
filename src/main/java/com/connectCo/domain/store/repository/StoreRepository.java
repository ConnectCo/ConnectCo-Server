package com.connectCo.domain.store.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    List<Store> findAllByMember(Member member);

    boolean existsStoreByName(String name);
}
