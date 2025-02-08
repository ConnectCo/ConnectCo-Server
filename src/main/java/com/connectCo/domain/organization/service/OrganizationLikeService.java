package com.connectCo.domain.organization.service;

import com.connectCo.domain.organization.entity.Organization;
import org.springframework.data.domain.Page;

public interface OrganizationLikeService {
    Boolean likeOrganization(Long storeId, Organization organization);
    Boolean isLikeOrganization(Long storeId, Organization organization);
    Page<Organization> getOrganizationsByLike(Long storeId, int page, int size);
}
