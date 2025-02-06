package com.connectCo.domain.store.service;

import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreLike;

public interface StoreLikeService {
    Boolean likeStore(Long organizationId, Store store);
    Boolean isLikeStore(Long organizationId, Store store);
}
