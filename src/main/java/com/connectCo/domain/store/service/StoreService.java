package com.connectCo.domain.store.service;


import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.request.StoreUpdateRequest;
import com.connectCo.domain.store.dto.response.StoreDetailInquiryResponse;
import com.connectCo.domain.store.dto.response.StoreIdResponse;
import com.connectCo.domain.store.dto.response.StoreLocationInquiryResponse;
import com.connectCo.domain.store.dto.response.StoreSummaryInquiryResponse;
import com.connectCo.domain.store.entity.Store;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreService {

    StoreIdResponse createStore(List<MultipartFile> storeImages, StoreCreateRequest request);
    StoreIdResponse updateStore(Long storeId, List<MultipartFile> newImages, StoreUpdateRequest request);
    StoreIdResponse deleteStore(Long storeId);
    Boolean likeStore(Long storeId);
    StoreDetailInquiryResponse inquiryStoreDetail(Long storeId);
    List<StoreSummaryInquiryResponse> inquiryStoreByLike();
    List<StoreSummaryInquiryResponse> inquiryStoreByMember();
    List<StoreLocationInquiryResponse> inquiryStoreByLocation(double latitude, double longitude, int radius);
    List<Store> getStoresByMember(Member member);
    Store loadStore(Long storeId);

    
}
