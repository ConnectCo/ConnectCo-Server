package com.connectCo.domain.organization.controller;

import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.global.common.BaseResponse;
import com.connectCo.global.common.dto.AddressRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "조직 API", description = "조직 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(summary = "조직 등록 API(관리자용)")
    @PostMapping
    public BaseResponse<OrganizationIdResponse> createOrganization(
            @RequestBody OrganizationCreateRequest request) {
        return BaseResponse.onSuccess(organizationService.createOrganization(request));
    }

    @Operation(summary = "조직 기본 정보 수정 API(관리자용)", description = "이름, url 정보만 수정 가능")
    @PatchMapping("/{organizationId}")
    public BaseResponse<OrganizationIdResponse> updateOrganizationInfo(
            @Parameter(description = "수정할 조직 id") @PathVariable Long organizationId,
            @RequestBody OrganizationUpdateRequest request) {
        return BaseResponse.onSuccess(organizationService.updateOrganizationInfo(organizationId, request));
    }

    @Operation(summary = "조직 주소 정보 수정 API(관리자용)", description = "주소 정보만 수정 가능")
    @PatchMapping("/{organizationId}/address")
    public BaseResponse<OrganizationIdResponse> updateOrganizationAddress(
            @Parameter(description = "수정할 조직 id") @PathVariable Long organizationId,
            @RequestBody AddressRequest request ) {
        return BaseResponse.onSuccess(organizationService.updateOrganizationAddress(organizationId, request));

    }

    @Operation(summary = "조직 삭제 API(관리자용)")
    @DeleteMapping("/{organizationId}")
    public BaseResponse<OrganizationIdResponse> deleteOrganization(
            @Parameter(description = "삭제할 조직 id") @PathVariable Long organizationId) {
        return BaseResponse.onSuccess(organizationService.deleteOrganization(organizationId));
    }

}
