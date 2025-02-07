package com.connectCo.domain.event.service;

import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventLike;
import com.connectCo.domain.event.mapper.EventMapper;
import com.connectCo.domain.event.repository.EventLikeRepository;
import com.connectCo.domain.store.entity.Store;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventLikeServiceImpl implements EventLikeService {

    private final EventLikeRepository eventLikeRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public Boolean likeEvent(Event event, Store store) {
        Optional<EventLike> eventLikeOptional = eventLikeRepository.findByStoreAndEvent(store, event);
        if (eventLikeOptional.isPresent()) {
            return eventLikeOptional.get().changeLike();
        }
        eventLikeRepository.save(eventMapper.toEventLike(store, event));
        return true;
    }

    @Override
    public Boolean isLikeEvent(Event event, Store store) {
        return eventLikeRepository.findByStoreAndEvent(store, event)
            .map(EventLike::getIsActive)
            .orElse(false);
    }

    @Override
    public Page<Event> getEventsByLike(Store store, Pageable pageable) {
        return eventLikeRepository.findAllByStoreAndIsActiveTrue(store, pageable)
            .map(EventLike::getEvent);
    }
}
