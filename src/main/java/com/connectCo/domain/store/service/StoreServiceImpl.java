package com.connectCo.domain.store.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.address.service.AddressService;
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.request.StoreUpdateRequest;
import com.connectCo.domain.store.dto.response.StoreDetailInquiryResponse;
import com.connectCo.domain.store.dto.response.StoreIdResponse;
import com.connectCo.domain.store.dto.response.StoreLocationInquiryResponse;
import com.connectCo.domain.store.dto.response.StoreSummaryInquiryResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreLikeRepository storeLikeRepository;
    private final StoreMapper storeMapper;
    private final StoreImageService storeImageService;

    private final AuthService authService;
    private final AddressService addressService;

    /*
     * 새로운 가게를 등록
     */
    @Override
    @Transactional
    public StoreIdResponse createStore(List<MultipartFile> storeImages, StoreCreateRequest request) {
        Member member = authService.getLoginMember();
        Address newAddress = addressService.createAddress(
                request.getDetailAddress(), request.getLatitude(), request.getLongitude());
        Store newStore = createAndSaveStore(member, request, newAddress);

        List<StoreImage> newStoreImages = (storeImages != null) ?
                storeImageService.createAndSaveStoreImages(newStore, storeImages) : List.of();
        newStore.changeImages(newStoreImages);
        return new StoreIdResponse(newStore.getId());
    }

    /*
     * 특정 가게 정보 업데이트
     */
    @Override
    @Transactional
    public StoreIdResponse updateStore(Long storeId, List<MultipartFile> newImages, StoreUpdateRequest request) {
        Member member = authService.getLoginMember();
        Store store = loadStore(storeId);

        // 수정 권한 유효성 검사(본인이 아닌 경우 수정 불가)
        ParamValidator.validModify(member.getId(), store.getMember().getId());

        // 주소 정보 업데이트
        store.getAddress().updateAddress(request.getDetailAddress(), request.getLatitude(), request.getLongitude());

        // 정보 수정
        store.updateStoreInfo(request);

        // 이미지 업데이트
        storeImageService.updateStoreImages(store, request.getExistingImages(), newImages);

        return new StoreIdResponse(store.getId());
    }

    /*
     * 특정 가게 삭제
     */
    @Override
    @Transactional
    public StoreIdResponse deleteStore(Long storeId) {
        Member member = authService.getLoginMember();
        Store store = loadStore(storeId);

        // 삭제 권한 유효성 검사
        ParamValidator.validModify(member.getId(), store.getMember().getId());

        // 가게 이미지 삭제
        storeImageService.deleteExistingImages(store.getImages());
        store.changeImages(List.of());

        // 가게 soft 삭제
        store.delete();

        return new StoreIdResponse(storeId);
    }

    /*
     * 특정 가게 찜하기
     */
    @Override
    @Transactional
    public Boolean likeStore(Long storeId) {
        Member member = authService.getLoginMember();
        Store store = loadStore(storeId);

        Optional<StoreLike> storeLike = storeLikeRepository.findByMemberAndStore(member, store);

        return storeLike.map(StoreLike::changeLike)
                .orElseGet(() -> storeLikeRepository.save(storeMapper.toStoreLike(store, member)).isChecked());

    }

    /*
     * 특정 가게 상세 조회
     */
    @Override
    public StoreDetailInquiryResponse inquiryStoreDetail(Long storeId) {
        Store store = loadStore(storeId);

        return storeMapper.toStoreDetailInquiryResponse(store,
                store.getImages().stream().map(StoreImage::getUrl).toList(),
                store.getCoupons().stream().map(storeMapper::toStoreCoupon).toList());
    }

    /*
     * 내가 찜한 가게 조회
     */
    @Override
    public List<StoreSummaryInquiryResponse> inquiryStoreByLike() {
        Member member = authService.getLoginMember();

        List<Store> storeList = storeLikeRepository.findAllByMemberAndIsChecked(member, true).stream()
                .map(StoreLike::getStore)
                .toList();
        return storeList.stream().map(storeMapper::toStoreSummaryInquiryResponse).toList();
    }

    /*
     * 나의 가게 조회
     */
    @Override
    public List<StoreSummaryInquiryResponse> inquiryStoreByMember() {
        Member member = authService.getLoginMember();
        return getStoresByMember(member).stream().map(storeMapper::toStoreSummaryInquiryResponse).toList();
    }

    /*
     * 내 주변 가게 목록 조회
     */
    @Override
    public List<StoreLocationInquiryResponse> inquiryStoreByLocation(double latitude, double longitude, int radius) {
        // 위도, 경도, 반경 값 유효성 검사
        ParamValidator.validLocation(latitude, longitude);
        ParamValidator.validRadius(radius);

        List<Object[]> results = storeRepository.findStoresByLocationWithDistance(latitude, longitude, radius);
        return results.stream()
                .map(result -> storeMapper.toStoreLocationInquiryResponse(
                        (Store) result[0], ((Number) result[1]).doubleValue()))
                .toList();
    }

    /*
     * 특정 member의 가게 목록을 조회
     */
    @Override
    public List<Store> getStoresByMember(Member member) {
        return storeRepository.findAllByMember(member);
    }

    /*
     * Store 객체를 생성하고 DB에 저장
     */
    private Store createAndSaveStore(Member member, StoreCreateRequest request, Address address) {
        Store store = storeMapper.toStore(member, request, address);
        return storeRepository.save(store);
    }

    /*
     * 가게 id로 가게 엔티티를 반환
     */
    public Store loadStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.STORE_NOT_FOUND));
    }
}
