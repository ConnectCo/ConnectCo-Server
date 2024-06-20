package com.connectCo.domain.store.controller;

import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.request.StoreUpdateRequest;
import com.connectCo.domain.store.dto.response.StoreDetailInquiryResponse;
import com.connectCo.domain.store.dto.response.StoreIdResponse;
import com.connectCo.domain.store.dto.response.StoreLocationInquiryResponse;
import com.connectCo.domain.store.dto.response.StoreSummaryInquiryResponse;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "가게 API", description = "가게 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "가게 등록 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<StoreIdResponse> createStore(
            @Parameter(description = "가게 이미지 파일들(없을 시 사용 x)") @RequestPart(value = "storeImages", required = false) List<MultipartFile> storeImages,
            @Parameter(description = "가게 생성 요청 json") @Valid @RequestPart(value = "request") StoreCreateRequest request) {
        return BaseResponse.onSuccess(storeService.createStore(storeImages, request));
    }

    @Operation(summary = "가게 수정 API", description = "본인만 가능")
    @PatchMapping(value = "/{storeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<StoreIdResponse> updateStore(
            @Parameter(description = "수정할 가게 id") @PathVariable Long storeId,
            @Parameter(description = "추가된 가게 이미지 파일들(없을 시 사용 x)") @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @Parameter(description = "가게 수정 요청 json") @Valid @RequestPart("request") StoreUpdateRequest request) {
        return BaseResponse.onSuccess(storeService.updateStore(storeId, newImages, request));
    }

    @Operation(summary = "가게 삭제 API", description = "본인만 가능")
    @DeleteMapping("/{storeId}")
    public BaseResponse<StoreIdResponse> deleteStore(
            @Parameter(description = "삭제할 가게 id") @PathVariable Long storeId) {
        return BaseResponse.onSuccess(storeService.deleteStore(storeId));
    }

    @Operation(summary = "가게 찜하기 API")
    @PostMapping("/{storeId}/like")
    public BaseResponse<Boolean> likeStore(
            @Parameter(description = "찜할 가게 id") @PathVariable Long storeId) {
        return BaseResponse.onSuccess(storeService.likeStore(storeId));
    }

    @Operation(summary = "가게 상세조회 API")
    @GetMapping("/{storeId}")
    public BaseResponse<StoreDetailInquiryResponse> inquiryStoreDetail(
            @Parameter(description = "조회할 가게 id") @PathVariable Long storeId) {
        return BaseResponse.onSuccess(storeService.inquiryStoreDetail(storeId));
    }


    @Operation(summary = "내 가게 조회 API")
    @GetMapping("/mine")
    public BaseResponse<List<StoreSummaryInquiryResponse>> inquiryStoreByMember() {
        return BaseResponse.onSuccess(storeService.inquiryStoreByMember());
    }

    @Operation(summary = "내가 찜한 가게 조회 API")
    @GetMapping("/like")
    public BaseResponse<List<StoreSummaryInquiryResponse>> inquiryStoreByLike() {
        return BaseResponse.onSuccess(storeService.inquiryStoreByLike());
    }

    @Operation(summary = "내 주변 가게 조회 API")
    @Parameters(value = {
            @Parameter(name = "latitude", description = "현재 유저 위도 위치입니다. (-90 ~ 90)"),
            @Parameter(name = "longitude", description = "현재 유저의 경도 위치입니다. (-180 ~ 180)"),
            @Parameter(name = "radius", description = "조회할 위치 반경입니다. (0보다 큰 정수)")
    })
    @GetMapping("/location")
    public BaseResponse<List<StoreLocationInquiryResponse>> inquiryStoreByLocation(
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "radius") int radius) {
        return BaseResponse.onSuccess(storeService.inquiryStoreByLocation(latitude, longitude, radius));
    }
}
