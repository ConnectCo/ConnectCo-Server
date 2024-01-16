package com.connectCo.domain.store.controller;

import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.response.StoreIdResponse;
import com.connectCo.domain.store.dto.response.StoreSummaryInquiryResponse;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.global.common.BaseResponse;
import com.connectCo.global.exception.CustomApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @PostMapping
    public BaseResponse<StoreIdResponse> createStore(@RequestPart(value = "storeImages", required = false) List<MultipartFile> storeImages,
                                                     @Valid @RequestPart("request") StoreCreateRequest request) {
        return BaseResponse.onSuccess(storeService.createStore(storeImages, request));
    }

    @Operation(summary = "내가 찜한 가게 조회 API")
    @GetMapping("/like")
    public BaseResponse<List<StoreSummaryInquiryResponse>> inquiryStoreByLike() {
        return BaseResponse.onSuccess(storeService.inquiryStoreByLike());
    }
}
