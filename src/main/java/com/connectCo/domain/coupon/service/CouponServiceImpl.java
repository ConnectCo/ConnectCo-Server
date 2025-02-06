package com.connectCo.domain.coupon.service;

import com.connectCo.domain.coupon.dto.request.CouponUpdateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailInquiryResponse;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.entity.CouponImage;
import com.connectCo.domain.coupon.entity.CouponLike;
import com.connectCo.domain.coupon.mapper.CouponMapper;
import com.connectCo.domain.coupon.repository.CouponImageRepository;
import com.connectCo.domain.coupon.repository.CouponLikeRepository;
import com.connectCo.domain.coupon.repository.CouponRepository;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.repository.StoreLikeRepository;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.global.validation.ParamValidator;
import com.connectCo.utils.S3FileComponent;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponLikeRepository couponLikeRepository;
    private final CouponMapper couponMapper;
    private final CouponImageRepository couponImageRepository;

    private final StoreService storeService;
    private final OrganizationService organizationService;
    private final S3FileComponent s3FileComponent;

    /*
     * 새로운 쿠폰을 등록하는 서비스 함수
     */
    @Override
    @Transactional
    public CouponIdResponse createCoupon(
        Long profileId, List<MultipartFile> couponImages, CouponCreateRequest request
    ) {
        Store store = storeService.loadStore(profileId);

        Coupon newCoupon = createAndSaveCoupon(store, request);
        store.addCoupon(newCoupon);
        if (couponImages != null) {
            List<CouponImage> newCouponImages = createAndSaveCouponImages(newCoupon, couponImages);
            newCoupon.changeImages(newCouponImages);
        }

        return new CouponIdResponse(newCoupon.getId());
    }

    /*
     * 쿠폰을 수정하는 서비스 함수
     */
    @Override
    @Transactional
    public CouponIdResponse updateCoupon(
        Long profileId, Long couponId, List<MultipartFile> couponImages, CouponUpdateRequest request
    ) {
        Store store = storeService.loadStore(profileId);
        // 수정 권한 유효성 검사(가게 주인만 수정 가능)
        Coupon coupon = couponRepository.getCoupon(couponId);
        ParamValidator.validModify(coupon.getStore().getId(), store.getId());

        // 쿠폰의 정보를 업데이트
        coupon.updateDetails(request);

        // 이미지 업데이트
        if (couponImages != null && !couponImages.isEmpty()) {
            updateCouponImages(coupon, request.getExistingImages(), couponImages);
        }

        return new CouponIdResponse(coupon.getId());
    }

    /*
     * 쿠폰을 삭제하는 서비스 함수
     */
    @Override
    @Transactional
    public CouponIdResponse deleteCoupon(Long profileId, Long couponId) {
        Store store = storeService.loadStore(profileId);
        // 삭제 권한 유효성 검사(가게 주인만 수정 가능)
        Coupon coupon = couponRepository.getCoupon(couponId);
        ParamValidator.validModify(coupon.getStore().getId(), store.getId());

        // 쿠폰 이미지 삭제
        deleteCouponImages(coupon.getImages());
        store.removeCoupon(coupon);

        // TODO: 관련된 찜 기록, 협찬 기록 등 삭제 로직 추가

        // 쿠폰은 hard delete
        couponRepository.delete(coupon);
        return new CouponIdResponse(couponId);
    }

    @Override
    @Transactional
    public Boolean likeCoupon(Long profileId, Long couponId) {
        Organization organization = organizationService.loadOrganization(profileId);
        Coupon coupon = couponRepository.getCoupon(couponId);
        return likeCoupon(organization, coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponDetailInquiryResponse inquiryCouponDetail(
        Long profileId, ProfileType profileType, Long couponId
    ) {
        Coupon coupon = couponRepository.getCoupon(couponId);

        // 본인 여부 확인
        Boolean isMine = coupon.getStore().getId().equals(profileId);

        // 찜 여부 확인
        Boolean isLiked = Boolean.FALSE;
        if (profileId != null && profileType != null && profileType.equals(ProfileType.ORGANIZATION)) {
            isLiked = isLikeCoupon(organizationService.loadOrganization(profileId), coupon);
        }

        return couponMapper.toCouponDetailResponse(coupon, isLiked, isMine);
    }

    @Override
    public CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponByLike(
        Long profileId, int page, int size
    ) {
        Organization organization = organizationService.loadOrganization(profileId);

        Page<Coupon> couponList =
            couponLikeRepository.findAllByOrganizationAndIsActiveTrue(organization, PageRequest.of(page, size))
                .map(CouponLike::getCoupon);

        return couponMapper.toCouponPagingResponse(
            couponList.map(couponMapper::toCouponSummaryInquiryResponse)
        );
    }

    @Override
    public List<CouponSummaryInquiryResponse> inquiryMyCoupon(Long profileId) {
        Store store = storeService.loadStore(profileId);

        return store.getCoupons().stream()
            .map(couponMapper::toCouponSummaryInquiryResponse)
            .toList();
    }

    @Override
    public CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponByStore(
        Long storeId, int page, int size
    ) {
        Store store = storeService.loadStore(storeId);

        return couponMapper.toCouponPagingResponse(
            inquiryCouponByStore(store, PageRequest.of(page, size))
                .map(couponMapper::toCouponSummaryInquiryResponse)
        );
    }

//
//
//
//    @Override
//    public List<CouponSummaryInquiryResponse> inquiryCouponByRecent() {
//        Pageable pageable= PageRequest.of(0,10);
//        List<Coupon> couponList=couponRepository.findAllByOrderByCreatedAtDesc(pageable).getContent();
//
//        return couponList.stream()
//                .map(couponMapper::toCouponSummaryInquiryResponse)
//                .toList();
//    }

    private Coupon createAndSaveCoupon(Store store, CouponCreateRequest request) {
        Coupon coupon = couponMapper.toCoupon(store, request);
        return couponRepository.save(coupon);
    }

    private List<CouponImage> createAndSaveCouponImages(Coupon newCoupon, List<MultipartFile> couponImages) {
        return couponImages.stream()
                .map(couponImage -> s3FileComponent.uploadFile("coupon", couponImage))
                .map(couponUrl -> couponMapper.toCouponImage(newCoupon, couponUrl))
                .map(couponImageRepository::save)
                .toList();
    }

    private void updateCouponImages(Coupon coupon, List<String> existingImageUrls, List<MultipartFile> newImages) {
        // 기존 이미지를 유지하거나 삭제
        List<CouponImage> existingImages = couponImageRepository.findAllByCoupon(coupon);
        List<CouponImage> existingImagesToKeep = existingImages.stream()
                .filter(image -> existingImageUrls.contains(image.getUrl()))
                .collect(Collectors.toList());

        List<CouponImage> imagesToRemove = existingImages.stream()
                .filter(image -> !existingImageUrls.contains(image.getUrl()))
                .toList();

        // 새로운 이미지 추가
        List<CouponImage> newCouponImages = (newImages != null)
                ? createAndSaveCouponImages(coupon, newImages) : List.of();

        // 기존 이미지와 새로운 이미지를 합침
        existingImagesToKeep.addAll(newCouponImages);

        // 삭제할 기존 이미지 삭제
        deleteCouponImages(imagesToRemove);

        coupon.changeImages(existingImagesToKeep);
    }

    private void deleteCouponImages(List<CouponImage> imagesToRemove) {
        for (CouponImage image : imagesToRemove) {
            s3FileComponent.deleteFile(image.getUrl());
            couponImageRepository.delete(image);
        }
    }

    private Boolean likeCoupon(Organization organization, Coupon coupon) {
        Optional<CouponLike> couponLikeOpt = couponLikeRepository.findByOrganizationAndCoupon(organization, coupon);
        if (couponLikeOpt.isPresent()) {
            return couponLikeOpt.get().changeLike();
        }

        couponLikeRepository.save(couponMapper.toCouponLike(coupon, organization));
        return true;
    }

    private Boolean isLikeCoupon(Organization organization, Coupon coupon) {
        return couponLikeRepository.findByOrganizationAndCoupon(organization, coupon)
            .map(CouponLike::getIsActive)
            .orElse(false);
    }

    private Page<Coupon> inquiryCouponByStore(Store store, Pageable pageable) {
        return couponRepository.findAllByStore(store, pageable);
    }
}
