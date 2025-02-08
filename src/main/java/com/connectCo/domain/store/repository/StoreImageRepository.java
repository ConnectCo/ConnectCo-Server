package com.connectCo.domain.store.repository;

import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
    List<StoreImage> findAllByStore(Store store);
}
