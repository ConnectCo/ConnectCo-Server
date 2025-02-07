package com.connectCo.domain.event.service;

import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.store.entity.Store;
import org.springframework.data.domain.Page;

public interface EventLikeService {
    Boolean likeEvent(Event event, Store store);
    Boolean isLikeEvent(Event event, Store store);
    Page<Event> getEventsByLike(Store store, int page, int size);
}
