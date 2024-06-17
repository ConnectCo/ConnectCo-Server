package com.connectCo.domain.organization.service;

import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;

public interface OrganizationService {
    OrganizationIdResponse createOrganization(OrganizationCreateRequest request);
}
