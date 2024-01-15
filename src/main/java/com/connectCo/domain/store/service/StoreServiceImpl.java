package com.connectCo.domain.store.service;

import com.connectCo.domain.Member.dto.response.MemberInfoResponse;
import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.response.StoreIdResponse;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import com.connectCo.domain.store.mapper.StoreMapper;
import com.connectCo.domain.store.repository.StoreImageRepository;
import com.connectCo.domain.store.repository.StoreRepository;
import com.connectCo.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final StoreMapper storeMapper;
    private final AuthService authService;
    private final S3FileComponent s3FileComponent;

    /*
     * 새로운 가게를 등록하는 서비스 함수
     */
    @Override
    @Transactional
    public StoreIdResponse createStore(List<MultipartFile> storeImages, StoreCreateRequest request) {
        Member member = authService.getLoginMember();

        Store newStore = createAndSaveStore(member, request);

        List<StoreImage> newStoreImages = createAndSaveStoreImages(newStore, storeImages);

        newStore.changeImages(newStoreImages);

        return new StoreIdResponse(newStore.getId());
    }


    @Override
    public List<MemberInfoResponse.MyStores> getStoresByMember(Member member) {
        return storeRepository.findAllByMember(member).stream()
                .map(storeMapper::toMyStores)
                .toList();
    }

    /*
     * Store 객체를 생성하고 DB에 저장하는 함수
     */
    private Store createAndSaveStore(Member member, StoreCreateRequest request) {
        Store store = storeMapper.toStore(member, request);
        return storeRepository.save(store);
    }

    /*
     * 가게 이미지 객체를 생성하고 DB에 저장하는 함수
     */
    private List<StoreImage> createAndSaveStoreImages(Store newStore, List<MultipartFile> storeImages) {
        return storeImages.stream()
                .map(storeImage -> s3FileComponent.uploadFile("store", storeImage))
                .map(storeUrl -> storeMapper.toStoreImage(newStore, storeUrl))
                .map(storeImageRepository::save)
                .toList();
    }
}
