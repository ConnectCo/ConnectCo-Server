package com.connectCo.domain.store.service;

import com.connectCo.domain.store.entity.Store;

public interface StoreLikeService {
    Boolean likeStore(Long organizationId, Store store);
}
