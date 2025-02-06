package com.connectCo.domain.store.service;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.service.AuthService;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.service.AddressService;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.request.StoreUpdateRequest;
import com.connectCo.domain.store.dto.response.*;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import com.connectCo.domain.store.entity.StoreLike;
import com.connectCo.domain.store.mapper.StoreMapper;
import com.connectCo.domain.store.repository.StoreLikeRepository;
import com.connectCo.domain.store.repository.StoreRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.global.validation.ParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final StoreImageService storeImageService;
    private final StoreLikeService storeLikeService;

    private final AddressService addressService;

    /*
     * 새로운 가게를 등록
     */
    @Override
    @Transactional
    public StoreIdResponse createStore(
        Member member, List<MultipartFile> storeImages, MultipartFile businessLicense, StoreCreateRequest request
    ) {
        // 가게 등록 제한 검사
        if (storeRepository.countByMember(member) >= 3) {
            throw new CustomApiException(ErrorCode.STORE_LIMIT_EXCEEDED);
        }

        Address newAddress = addressService.createAddress(
                request.getDetailAddress(), request.getLatitude(), request.getLongitude()
        );

        // TODO: 사업자 등록증 심사 로직 추가

        Store newStore = createAndSaveStore(member, request, newAddress);

        if (storeImages != null) {
            List<StoreImage> andSaveStoreImages = storeImageService.createAndSaveStoreImages(newStore, storeImages);
            newStore.updateProfileImage(andSaveStoreImages.get(0).getUrl());
        }

        return new StoreIdResponse(newStore.getId());
    }

    /*
     * 특정 가게 정보 업데이트
     */
    @Override
    @Transactional
    public StoreIdResponse updateStore(
        Member member, Long storeId, List<MultipartFile> newImages, StoreUpdateRequest request
    ) {
        Store store = loadStore(storeId);
        // 수정 권한 유효성 검사(본인이 아닌 경우 수정 불가)
        ParamValidator.validModify(member.getId(), store.getMember().getId());
        if (storeRepository.existsByName(request.getName()) && !store.getName().equals(request.getName())) {
            throw new CustomApiException(ErrorCode.STORE_NAME_DUPLICATION);
        }

        // 주소 정보 업데이트
        store.getAddress().updateAddress(request.getDetailAddress(), request.getLatitude(), request.getLongitude());
        // 정보 수정
        store.updateStoreInfo(request);

        // 이미지 업데이트
        if (newImages != null || !request.getExistingImages().isEmpty() ) {
            String profileUrl = storeImageService.updateStoreImages(store, request.getExistingImages(), newImages);
            store.updateProfileImage(profileUrl);
        }

        return new StoreIdResponse(store.getId());
    }

    /*
     * 특정 가게 삭제
     */
    @Override
    @Transactional
    public StoreIdResponse deleteStore(Member member, Long storeId) {
        Store store = loadStore(storeId);
        // 삭제 권한 유효성 검사
        ParamValidator.validModify(member.getId(), store.getMember().getId());

        // 가게 이미지 삭제
        storeImageService.deleteImages(store);
        // TODO: 관련된 가게, 가게 리뷰, 찜 기록 등 삭제 로직 추가

        store.delete();
        store.updateProfileImage(null);

        return new StoreIdResponse(storeId);
    }

    /*
     * 특정 가게 찜하기
     */
    @Override
    @Transactional
    public Boolean likeStore(Long profileId, Long storeId) {
        Store store = loadStore(storeId);
        return storeLikeService.likeStore(profileId, store);
    }

    /*
     * 특정 가게 상세 조회
     */
    @Override
    public StoreDetailInquiryResponse inquiryStoreDetail(
        Long profileId, ProfileType profileType, Long storeId
    ) {
        // 본인 여부 확인
        Boolean isMine = profileId != null && profileId.equals(storeId);
        Store store = loadStore(storeId);

        // 찜 여부 확인
        Boolean isLiked = Boolean.FALSE;
        if (profileId != null && profileType != null && profileType.equals(ProfileType.ORGANIZATION)) {
            isLiked = storeLikeService.isLikeStore(profileId, store);
        }

        return storeMapper.toStoreDetailInquiryResponse(
            store, storeImageService.getStoreImageUrls(store), isLiked, isMine,
            store.getCoupons().stream().limit(2).map(storeMapper::toStoreCoupon).toList()
        );
    }

    /*
     * 내가 찜한 가게 조회
     */
    @Override
    public StorePagingResponse<StoreSummaryInquiryResponse> inquiryStoresByLike(
        Long profileId, int page, int size
    ) {
        Page<Store> storePage = storeLikeService.getStoresByLike(profileId, page, size);

        return storeMapper.toStorePagingResponse(
            storePage.map(storeMapper::toStoreSummaryInquiryResponse)
        );
    }

    private Store createAndSaveStore(Member member, StoreCreateRequest request, Address address) {
        Store store = storeMapper.toStore(member, request, address);
        return storeRepository.save(store);
    }

    public Store loadStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.STORE_NOT_FOUND));
    }
}
