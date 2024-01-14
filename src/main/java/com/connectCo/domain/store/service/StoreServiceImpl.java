package com.connectCo.domain.store.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public List<String> getStoresByMember(Member member) {
        return storeRepository.findAllByMember(member).stream()
                .map(Store::getName)
                .toList();
    }
}
