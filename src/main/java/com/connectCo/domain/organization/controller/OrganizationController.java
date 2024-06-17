package com.connectCo.domain.organization.controller;

import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "조직 API", description = "조직 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(summary = "조직 등록 API", description = "관리자 계정만 가능")
    @PostMapping
    public BaseResponse<OrganizationIdResponse> createOrganization(
            @RequestBody OrganizationCreateRequest request) {
        return BaseResponse.onSuccess(organizationService.createOrganization(request));
    }

}
