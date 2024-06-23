package com.connectCo.domain.store.repository;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByMember(Member member);
    Page<Store> findAllByMember(Member member, Pageable pageable);

    boolean existsStoreByName(String name);

    @Query(value = "SELECT s.*, " +
            "ST_Distance_Sphere(POINT(:longitude, :latitude), a.location) / 1000 AS distance " +
            "FROM Store s " +
            "JOIN Address a ON s.address.id = a.id " +
            "WHERE ST_DWithin(a.location, POINT(:longitude, :latitude), :radius * 1000) " +
            "ORDER BY distance", nativeQuery = true)
    Page<Object[]> findStoresByLocationWithDistance(@Param("latitude") double latitude,
                                                    @Param("longitude") double longitude,
                                                    @Param("radius") double radius,
                                                    Pageable pageable);
}
