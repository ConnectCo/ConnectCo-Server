package com.connectCo.domain.store.repository;

import com.connectCo.domain.store.entity.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreImageRepository extends JpaRepository<StoreImage, UUID> {
}
