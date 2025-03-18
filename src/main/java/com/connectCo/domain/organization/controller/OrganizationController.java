package com.connectCo.domain.organization.controller;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.organization.dto.request.OrganizationCreateRequest;
import com.connectCo.domain.organization.dto.request.OrganizationUpdateRequest;
import com.connectCo.domain.organization.dto.response.OrganizationIdResponse;
import com.connectCo.domain.organization.dto.response.OrganizationDetailInquiryResponse;
import com.connectCo.domain.organization.dto.response.OrganizationPagingResponse;
import com.connectCo.domain.organization.dto.response.OrganizationSummaryInquiryResponse;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

    @Operation(summary = "조직 삭제 API", description = "본인만 가능")
    @DeleteMapping("/{organizationId}")
    public BaseResponse<OrganizationIdResponse> deleteOrganization(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Parameter(description = "삭제할 조직 id") @PathVariable Long organizationId
    ) {
        return BaseResponse.onSuccess(organizationService.deleteOrganization(principal.member(), organizationId));
    }

    @Operation(summary = "조직 찜하기 API", description = "가게 프로필만 가능")
    @PostMapping("/{organizationId}/like")
    public BaseResponse<Boolean> likeOrganization(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Parameter(description = "찜할 조직 id") @PathVariable Long organizationId
    ) {
        return BaseResponse.onSuccess(organizationService.likeOrganization(principal.profileId(), organizationId));
    }

    @Operation(summary = "조직 상세 조회 API", description = "비로그인 시도 가능")
    @GetMapping("/{organizationId}/detail")
    public BaseResponse<OrganizationDetailInquiryResponse> inquiryOrganizationDetail(
            @AuthenticationPrincipal PrincipalDetails principal,
            @Parameter(description = "조회할 조직 이름") @PathVariable Long organizationId
    ) {
        if (principal == null) {
            return BaseResponse.onSuccess(
                organizationService.inquiryOrganizationDetail(null, null, organizationId)
            );
        }
        return BaseResponse.onSuccess(
            organizationService.inquiryOrganizationDetail(principal.profileId(), principal.profileType(), organizationId)
        );
    }

    @Operation(summary = "내가 찜한 조직 조회 API", description = "가게 프로필만 가능")
    @Parameters(value = {
        @Parameter(name = "page", description = "페이지 번호(0부터 시작)"),
        @Parameter(name = "size", description = "한 페이지 당 이벤트 개수"),
    })
    @GetMapping("/like")
    public BaseResponse<OrganizationPagingResponse<OrganizationSummaryInquiryResponse>> inquiryOrganizationsByLike(
        @AuthenticationPrincipal PrincipalDetails principal,
        @RequestParam(name = "page") int page,
        @RequestParam(name = "size") int size
    ) {
        return BaseResponse.onSuccess(
            organizationService.inquiryOrganizationsByLike(principal.profileId(), page, size)
        );
    }

    @Operation(summary = "대학 인증 요청 API (인증코드 발송)", description = "조직 프로필만 가능")
    @PostMapping("/{organizationId}/university-verify")
    public BaseResponse<Boolean> requestUniversityVerification(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long organizationId,
            @Valid @RequestBody UniversityVerificationRequest request) {
        
        // 권한 검증 - 본인의 조직만 인증 가능
        ParamValidator.validModify(principal.profileId(), organizationId);
        
        return BaseResponse.onSuccess(
                organizationService.requestUniversityVerification(organizationId, request.getEmail())
        );
    }

    @Operation(summary = "대학 인증 코드 확인 API", description = "조직 프로필만 가능")
    @PostMapping("/{organizationId}/university-verify-code")
    public BaseResponse<Boolean> verifyUniversityCode(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long organizationId,
            @Valid @RequestBody VerificationCodeRequest request) {
            
        // 권한 검증 - 본인의 조직만 인증 가능
        ParamValidator.validModify(principal.profileId(), organizationId);
        
        return BaseResponse.onSuccess(
                organizationService.verifyUniversityCode(organizationId, request.getCode())
        );
    }

    @Operation(summary = "대학 인증 상태 조회 API", description = "조직 프로필만 가능")
    @GetMapping("/{organizationId}/university-status")
    public BaseResponse<UniversityVerificationStatus> getUniversityVerificationStatus(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long organizationId) {
            
        // 권한 검증 - 본인의 조직만 확인 가능
        ParamValidator.validModify(principal.profileId(), organizationId);
        
        return BaseResponse.onSuccess(
                organizationService.getVerificationStatus(organizationId)
        );
    }

}
