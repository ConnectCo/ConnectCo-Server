package com.connectCo.domain.organization.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.entity.Role;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.service.AddressService;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.mapper.OrganizationMapper;
import com.connectCo.domain.organization.repository.OrganizationRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    private final AuthService authService;
    private final AddressService addressService;

    @Override
    public OrganizationIdResponse createOrganization(OrganizationCreateRequest request) {

        Member member = authService.getLoginMember();
        validateAdmin(member);

        Address address = addressService.createAddress(request.getDetailAddress(), request.getLatitude(), request.getLongitude());
        Organization newOrganization = organizationRepository.save(organizationMapper.toOrganization(request, address));

        return new OrganizationIdResponse(newOrganization.getId());
    }

    private void validateAdmin(Member member) {
        if (!member.getRole().equals(Role.ADMIN))
            throw new CustomApiException(ErrorCode.USER_NOT_ADMIN);
    }
}
