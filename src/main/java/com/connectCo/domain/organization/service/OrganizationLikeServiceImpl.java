package com.connectCo.domain.organization.service;

import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.entity.OrganizationLike;
import com.connectCo.domain.organization.mapper.OrganizationMapper;
import com.connectCo.domain.organization.repository.OrganizationLikeRepository;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.repository.StoreRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationLikeServiceImpl implements OrganizationLikeService {
    private final OrganizationLikeRepository organizationLikeRepository;
    private final StoreRepository storeRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    @Transactional
    public Boolean likeOrganization(Long storeId, Organization organization) {
        Store store = storeRepository.getStore(storeId);
        Optional<OrganizationLike> organizationLikeOpt
            = organizationLikeRepository.findByOrganizationAndStore(organization, store);
        if (organizationLikeOpt.isPresent()) {
            return organizationLikeOpt.get().changeLike();
        }
        organizationLikeRepository.save(organizationMapper.toOrganizationLike(organization, store));
        return true;
    }

    @Override
    public Boolean isLikeOrganization(Long storeId, Organization organization) {
        Store store = storeRepository.getStore(storeId);
        return organizationLikeRepository.findByOrganizationAndStore(organization, store)
            .map(OrganizationLike::getIsActive)
            .orElse(false);
    }

    @Override
    public Page<Organization> getOrganizationsByLike(Long storeId, int page, int size) {
        Store store = storeRepository.getStore(storeId);
        return organizationLikeRepository.findAllByStoreAndIsActiveTrue(store, PageRequest.of(page, size))
            .map(OrganizationLike::getOrganization);
    }
}
