package com.connectCo.domain.coupon.service;

import com.connectCo.config.security.auth.PrincipalDetails;
import com.connectCo.domain.address.entity.Address;
import com.connectCo.domain.coupon.dto.request.CouponUpdateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailInquiryResponse;
import com.connectCo.domain.coupon.dto.response.CouponPagingResponse;
import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.entity.CouponImage;
import com.connectCo.domain.coupon.entity.CouponLike;
import com.connectCo.domain.coupon.entity.CouponSearchType;
import com.connectCo.domain.coupon.mapper.CouponMapper;
import com.connectCo.domain.coupon.repository.CouponImageRepository;
import com.connectCo.domain.coupon.repository.CouponLikeRepository;
import com.connectCo.domain.coupon.repository.CouponRepository;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.domain.member.repository.ProfileRepository;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.organization.service.OrganizationService;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.repository.StoreLikeRepository;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
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
    private final ProfileRepository profileRepository;

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
    public CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponsByLike(
        Long profileId, int page, int size
    ) {
        Organization organization = organizationService.loadOrganization(profileId);

        Page<Coupon> couponPage =
            couponLikeRepository.findAllByOrganizationAndIsActiveTrue(organization, PageRequest.of(page, size))
                .map(CouponLike::getCoupon);

        return couponMapper.toCouponPagingResponse(
            couponPage.map(couponMapper::toCouponSummaryInquiryResponse)
        );
    }

    @Override
    public CouponPagingResponse<CouponSummaryInquiryResponse> inquiryMyCoupons(
        Long profileId, int page, int size
    ) {
        Store store = storeService.loadStore(profileId);
        Page<Coupon> couponPage = inquiryCouponByStore(store, PageRequest.of(page, size));

        return couponMapper.toCouponPagingResponse(
            couponPage.map(couponMapper::toCouponSummaryInquiryResponse)
        );
    }

    @Override
    public CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCouponsByStore(
        Long storeId, int page, int size
    ) {
        Store store = storeService.loadStore(storeId);

        return couponMapper.toCouponPagingResponse(
            inquiryCouponByStore(store, PageRequest.of(page, size))
                .map(couponMapper::toCouponSummaryInquiryResponse)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CouponPagingResponse<CouponSummaryInquiryResponse> inquiryCoupons(
        PrincipalDetails principal, CouponSearchType type, Double latitude, Double longitude, int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // 비로그인 시에는 latitude, longitude가 반드시 존재해야 함.
        if (principal == null) {
            if (latitude == null || longitude == null) {
                throw new CustomApiException(ErrorCode.IS_MUST_INPUT_LOCATION);
            }
            ParamValidator.validLocation(latitude, longitude);
            return getCouponsByLocation(latitude, longitude, type, pageable);
        }

        // 로그인한 경우
        Optional<Profile> profileOptional =
            profileRepository.findByIdAndProfileType(principal.profileId(), principal.profileType());

        // latitude, longitude가 주어지면 해당 위치 기준으로 조회
        if (latitude != null && longitude != null) {
            ParamValidator.validLocation(latitude, longitude);
            return getCouponsByLocation(latitude, longitude, type, pageable);
        }

        // latitude, longitude가 없는 경우, profile에서 address 정보 가져오기
        Address address = getAddressFromProfile(profileOptional.orElse(null));
        if (address == null) {
            throw new CustomApiException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        return getCouponsByLocation(address.getLatitude(), address.getLongitude(), type, pageable);

    }

    private Address getAddressFromProfile(Profile profile) {
        if (profile == null) {
            return null;
        }

        if (profile instanceof Store store) {
            return store.getAddress();
        } else if (profile instanceof Organization organization) {
            return organization.getAddress();
        }
        return null;
    }

    private CouponPagingResponse<CouponSummaryInquiryResponse> getCouponsByLocation(
        double latitude, double longitude, CouponSearchType type, Pageable pageable) {

        Page<Coupon> couponPage = switch (type) {
            case RECENCY -> couponRepository.findAllByOrderByCreatedAtDesc(pageable);
            case DISTANCE -> couponRepository.findByDistance(latitude, longitude, pageable);
            case DEADLINE -> couponRepository.findAllByOrderByExpiredAtAsc(pageable);
            default -> throw new CustomApiException(ErrorCode.COUPON_SEARCH_TYPE_INVALID);
        };

        return couponMapper.toCouponPagingResponse(
            couponPage.map(couponMapper::toCouponSummaryInquiryResponse)
        );
    }

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
