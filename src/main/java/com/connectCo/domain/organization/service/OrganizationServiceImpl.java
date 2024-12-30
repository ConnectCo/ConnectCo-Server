package com.connectCo.domain.organization.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.entity.Role;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.service.AddressService;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.dto.response.OrganizationInquiryResponse;
import com.connectCo.domain.organization.dto.response.OrganizationSearchResponse;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.mapper.OrganizationMapper;
import com.connectCo.domain.organization.repository.OrganizationRepository;
import com.connectCo.global.common.dto.AddressRequest;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    private final AuthService authService;
    private final AddressService addressService;

    @Override
    public OrganizationIdResponse createOrganization(OrganizationCreateRequest request) {

        validateAdmin();

        Address address = addressService.createAddress(request.getDetailAddress(), request.getLatitude(), request.getLongitude());
        Organization newOrganization = organizationRepository.save(organizationMapper.toOrganization(request, address));

        return new OrganizationIdResponse(newOrganization.getId());
    }

    @Override
    @Transactional
    public OrganizationIdResponse updateOrganizationInfo(Long organizationId, OrganizationUpdateRequest request) {

        validateAdmin();

        Organization organization = loadOrganization(organizationId);
        organization.updateOrganizationInfo(request);

        return new OrganizationIdResponse(organization.getId());
    }

    @Override
    @Transactional
    public OrganizationIdResponse updateOrganizationAddress(Long organizationId, AddressRequest request) {

        validateAdmin();

        Organization organization = loadOrganization(organizationId);
        organization.getAddress().updateAddress(request.getDetailAddress(), request.getLatitude(), request.getLongitude());

        return new OrganizationIdResponse(organization.getId());
    }

    @Override
    @Transactional
    public OrganizationIdResponse deleteOrganization(Long organizationId) {

        validateAdmin();

        Organization organization = loadOrganization(organizationId);

        //TODO 관련된 Event들 다 삭제 처리되는지 확인 필요

        organization.delete();

        return new OrganizationIdResponse(organization.getId());
    }

    @Override
    public OrganizationInquiryResponse inquiryOrganization(String organizationName) {

        Organization organization = loadOrganizationByName(organizationName);

        return organizationMapper.toOrganizationInquiryResponse(organization);
    }

    @Override
    public List<OrganizationSearchResponse> searchOrganization(String keyword) {

        List<Organization> organizations = organizationRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(keyword);

        return organizations.stream().map(organizationMapper::toOrganizationSearchResponse).toList();
    }

    @Override
    public Organization loadOrganization(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.ORGANIZATION_NOT_FOUND));
    }

    @Override
    public Organization loadOrganizationByName(String name) {
        return organizationRepository.findOrganizationByName(name)
                .orElseThrow(() -> new CustomApiException(ErrorCode.ORGANIZATION_NOT_FOUND));
    }

    @Override
    public Organization loadOrganizationByMember(Member member) {
        return organizationRepository.findOrganizationByMember(member)
                .orElseThrow(() -> new CustomApiException(ErrorCode.ORGANIZATION_NOT_FOUND));
    }


    private void validateAdmin() {
        Member member = authService.getLoginMember();
        if (!member.getRole().equals(Role.ADMIN))
            throw new CustomApiException(ErrorCode.USER_NOT_ADMIN);
    }
}
