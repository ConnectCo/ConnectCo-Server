package com.connectCo.domain.store.service;


import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.request.StoreUpdateRequest;
import com.connectCo.domain.store.dto.response.*;
import com.connectCo.domain.store.entity.Store;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreService {

    StoreIdResponse createStore(
        Member member, List<MultipartFile> storeImages, MultipartFile businessLicense, StoreCreateRequest request
    );
    StoreIdResponse updateStore(
        Member member, Long storeId, List<MultipartFile> newImages, StoreUpdateRequest request
    );
    StoreIdResponse deleteStore(Member member, Long storeId);
    Boolean likeStore(Long organizationId, Long storeId);
//    StoreDetailInquiryResponse inquiryStoreDetail(Long storeId);
//    StorePagingResponse<StoreSummaryInquiryResponse> inquiryStoreByLike(int page, int size);
//    StorePagingResponse<StoreSummaryInquiryResponse> inquiryStoreByMember(int page, int size);
//    StorePagingResponse<StoreLocationInquiryResponse> inquiryStoreByLocation(double latitude, double longitude, int radius, int page, int size);
//    List<Store> getStoresByMember(Member member);
//    Store loadStore(Long storeId);

    
}
