package com.connectCo.domain.store.service;

import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreLike;
import com.connectCo.domain.store.mapper.StoreMapper;
import com.connectCo.domain.store.repository.StoreLikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreLikeServiceImpl implements StoreLikeService {
    private final StoreLikeRepository storeLikeRepository;
    private final OrganizationService organizationService;
    private final StoreMapper storeMapper;

    @Override
    @Transactional
    public Boolean likeStore(Long organizationId, Store store) {
        Organization organization = organizationService.loadOrganization(organizationId);
        Optional<StoreLike> storeLikeOpt = storeLikeRepository.findByOrganizationAndStore(organization, store);
        if (storeLikeOpt.isPresent()) {
            storeLikeOpt.get().changeLike();
            return storeLikeOpt.get().isActive();
        }

        storeLikeRepository.save(storeMapper.toStoreLike(organization, store));
        return true;
    }

    @Override
    public Boolean isLikeStore(Long organizationId, Store store) {
        return storeLikeRepository.findByOrganizationAndStore(organizationService.loadOrganization(organizationId), store)
            .map(StoreLike::isActive)
            .orElse(false);
    }
}
