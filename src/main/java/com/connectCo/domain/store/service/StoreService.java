package com.connectCo.domain.store.service;


import com.connectCo.domain.Member.dto.response.MemberInfoResponse;
import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.response.StoreIdResponse;
import com.connectCo.domain.store.entity.Store;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreService {

    StoreIdResponse createStore(List<MultipartFile> storeImages, StoreCreateRequest request);
    List<Store> getStoresByMember(Member member);
}
