package com.connectCo.domain.organization.controller;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@Tag(name = "조직 API", description = "조직 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(summary = "조직 등록 API", description = "로그인한 회원만 가능")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<OrganizationIdResponse> createOrganization(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Parameter(description = "조직 프로필 이미지 파일(없을 시 사용 x)") @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @Parameter(description = "조직 생성 요청 json") @RequestPart OrganizationCreateRequest request
    ) {
        return BaseResponse.onSuccess(
            organizationService.createOrganization(principal.member(), profileImage, request)
        );
    }

    @Operation(summary = "조직 기본 정보 수정 API", description = "본인만 가능")
    @PatchMapping("/{organizationId}")
    public BaseResponse<OrganizationIdResponse> updateOrganization(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Parameter(description = "수정할 조직 id") @PathVariable Long organizationId,
            @Parameter(description = "조직 프로필 이미지 파일(없을 시 사용 x)") @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @Parameter(description = "조직 수정 요청 json") @RequestPart("request") OrganizationUpdateRequest request) {
        return BaseResponse.onSuccess(
            organizationService.updateOrganization(principal.member(), organizationId, profileImage, request)
        );
    }
//
//    @Operation(summary = "조직 주소 정보 수정 API(관리자용)", description = "주소 정보만 수정 가능")
//    @PatchMapping("/{organizationId}/address")
//    public BaseResponse<OrganizationIdResponse> updateOrganizationAddress(
//            @Parameter(description = "수정할 조직 id") @PathVariable Long organizationId,
//            @RequestBody AddressRequest request ) {
//        return BaseResponse.onSuccess(organizationService.updateOrganizationAddress(organizationId, request));
//
//    }
//
//    @Operation(summary = "조직 삭제 API(관리자용)")
//    @DeleteMapping("/{organizationId}")
//    public BaseResponse<OrganizationIdResponse> deleteOrganization(
//            @Parameter(description = "삭제할 조직 id") @PathVariable Long organizationId) {
//        return BaseResponse.onSuccess(organizationService.deleteOrganization(organizationId));
//    }
//
//    @Operation(summary = "조직 상세 조회 API")
//    @GetMapping("/{organizationName}")
//    public BaseResponse<OrganizationInquiryResponse> inquiryOrganization(
//            @Parameter(description = "조회할 조직 이름") @PathVariable String organizationName) {
//        return BaseResponse.onSuccess(organizationService.inquiryOrganization(organizationName));
//    }
//
//    @Operation(summary = "조직 검색 API", description = "이름 오름차순으로 정렬")
//    @Parameters(value = {
//            @Parameter(name = "keyword", description = "이름에 포함되는 키워드로 한글자 이상 입력"),
//    })
//    @GetMapping("/search")
//    public BaseResponse<List<OrganizationSearchResponse>> searchOrganization(
//            @RequestParam(name = "keyword") String keyword) {
//        return BaseResponse.onSuccess(organizationService.searchOrganization(keyword));
//    }
}
