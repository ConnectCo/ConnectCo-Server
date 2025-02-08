package com.connectCo.domain.organization.mapper;

import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationDetailInquiryResponse;
import com.connectCo.domain.organization.dto.response.OrganizationPagingResponse;
import com.connectCo.domain.organization.dto.response.OrganizationSummaryInquiryResponse;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.entity.OrganizationLike;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.global.common.mapper.CommonMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {
    public Organization toOrganization(Member member, OrganizationCreateRequest request, Address address) {
        return Organization.builder()
            .name(request.getName())
            .phoneNumber(request.getPhoneNumber())
            .email(request.getEmail())
            .address(address)
            .description(request.getDescription())
            .member(member)
            .profileType(ProfileType.ORGANIZATION)
            .build();
    }

    public OrganizationLike toOrganizationLike(Organization organization, Store store) {
        return OrganizationLike.builder()
            .organization(organization)
            .store(store)
            .isActive(true)
            .build();
    }

    public <T>OrganizationPagingResponse<T> tOrganizationPagingResponse(Page<T> organizations) {
        return OrganizationPagingResponse.<T>builder()
            .organizations(organizations.getContent())
            .page(organizations.getNumber())
            .totalPages(organizations.getTotalPages())
            .totalElements((int) organizations.getTotalElements())
            .isFirst(organizations.isFirst())
            .isLast(organizations.isLast())
            .build();
    }

    public OrganizationSummaryInquiryResponse toOrganizationSummaryInquiryResponse(Organization organization) {
        // 유효한 이벤트 개수
        long validEventCount = organization.getEvents().stream()
            .filter(event->
                event.getExpiredAt().isAfter(LocalDate.now()) ||
                event.getExpiredAt().isEqual(LocalDate.now())
            ).count();

        return OrganizationSummaryInquiryResponse.builder()
            .organizationId(organization.getId())
            .name(organization.getName())
            .description(organization.getDescription())
            .thumbnail(organization.getProfileImage())
            .eventCount(validEventCount)
            .build();
    }

    public OrganizationDetailInquiryResponse toOrganizationDetailInquiryResponse(
        Organization organization, Boolean isLike, Boolean isMine,
        List<OrganizationDetailInquiryResponse.OrganizationEvent> events
    ) {
        return OrganizationDetailInquiryResponse.builder()
            .organizationId(organization.getId())
            .name(organization.getName())
            .description(organization.getDescription())
            .address(CommonMapper.toAddressResponse(organization.getAddress()))
            .phoneNumber(organization.getPhoneNumber())
            .profileImage(organization.getProfileImage())
            .events(events)
            .appliedCouponCount(organization.getAppliedCouponCount())
            .isLike(isLike)
            .isMine(isMine)
            .build();
    }


    public OrganizationDetailInquiryResponse.OrganizationEvent toOrganizationEvent(Event event) {
        String eventThumbnail = (event.getImages() != null && !event.getImages().isEmpty())
            ? event.getImages().get(0).getUrl()
            : null;

        return OrganizationDetailInquiryResponse.OrganizationEvent.builder()
            .eventId(event.getId())
            .name(event.getName())
            .eventThumbnail(eventThumbnail)
            .expiredAt(event.getExpiredAt())
            .build();
    }
}
