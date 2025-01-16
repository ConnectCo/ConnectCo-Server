package com.connectCo.domain.coupon.service;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.service.AuthService;
import com.connectCo.domain.coupon.dto.request.CouponCreateRequest;
import com.connectCo.domain.coupon.dto.response.CouponDetailResponse;
import com.connectCo.domain.coupon.dto.response.CouponIdResponse;
import com.connectCo.domain.coupon.dto.response.CouponSummaryInquiryResponse;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.entity.CouponImage;
import com.connectCo.domain.coupon.entity.CouponLike;
import com.connectCo.domain.coupon.mapper.CouponMapper;
import com.connectCo.domain.coupon.repository.CouponImageRepository;
import com.connectCo.domain.coupon.repository.CouponLikeRepository;
import com.connectCo.domain.coupon.repository.CouponRepository;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import com.connectCo.utils.S3FileComponent;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final StoreService storeService;
    private final AuthService authService;
    private final CouponRepository couponRepository;
    private final CouponLikeRepository couponLikeRepository;
    private final CouponMapper couponMapper;
    private final S3FileComponent s3FileComponent;
    private final CouponImageRepository couponImageRepository;

    @Override
    public List<CouponSummaryInquiryResponse> inquiryCouponByMember() {
        Member member = authService.getLoginMember();

        List<Coupon> couponList = storeService.getStoresByMember(member).stream()
                .flatMap(store -> couponRepository.findAllByStore(store).stream())
                .toList();

        return couponList.stream()
                .map(couponMapper::toCouponSummaryInquiryResponse)
                .toList();
    }

    @Override
    public List<CouponSummaryInquiryResponse> inquiryCouponByLike() {
        Member member = authService.getLoginMember();

        List<Coupon> couponList = couponLikeRepository.findAllByMemberAndIsChecked(member, true).stream()
                .map(CouponLike::getCoupon)
                .toList();

        return couponList.stream()
                .map(couponMapper::toCouponSummaryInquiryResponse)
                .toList();
    }

    @Override
    public List<CouponSummaryInquiryResponse> inquiryCouponByRecent() {
        Pageable pageable= PageRequest.of(0,10);
        List<Coupon> couponList=couponRepository.findAllByOrderByCreatedAtDesc(pageable).getContent();

        return couponList.stream()
                .map(couponMapper::toCouponSummaryInquiryResponse)
                .toList();
    }

    /*
     * 새로운 쿠폰을 등록하는 서비스 함수
     */
    @Override
    @Transactional
    public CouponIdResponse createCoupon(List<MultipartFile> couponImages, CouponCreateRequest request) {

        Store store = storeService.loadStore(request.getStoreId());
        //storeRepository에 접근해서 클라이언트에서 받은 storeid를 가지고 jpa를 통해 store객체 찾기

        Coupon newCoupon = createAndSaveCoupon(store, request);

        List<CouponImage> newCouponImages = createAndSaveCouponImages(newCoupon, couponImages);

        newCoupon.changeImages(newCouponImages);

        return new CouponIdResponse(newCoupon.getId());
    }

    /*
     * Coupon 객체를 생성하고 DB에 저장하는 함수
     */
    private Coupon createAndSaveCoupon(Store store, CouponCreateRequest request) {
        Coupon coupon = couponMapper.toCoupon(store, request);
        return couponRepository.save(coupon);
    }

    /*
     * 쿠폰 이미지 객체를 생성하고 DB에 저장하는 함수
     */
    private List<CouponImage> createAndSaveCouponImages(Coupon newCoupon, List<MultipartFile> couponImages) {
        return couponImages.stream()
                .map(couponImage -> s3FileComponent.uploadFile("coupon", couponImage))
                .map(couponUrl -> couponMapper.toCouponImage(newCoupon, couponUrl))
                .map(couponImageRepository::save)
                .toList();
    }


    @Override
    @Transactional
    public CouponIdResponse deleteCoupon(Long couponId) {
        Member member= authService.getLoginMember();

        Coupon coupon=couponRepository.findById(couponId).orElseThrow(() -> new CustomApiException(ErrorCode.COUPON_NOT_FOUND));
        if(!coupon.getStore().getMember().equals(member)){
            throw new CustomApiException(ErrorCode.INVALID_PERMISSION);
        }

        Long deletedCouponId= coupon.getId();
        couponRepository.deleteById(deletedCouponId);
        return new CouponIdResponse(deletedCouponId);
    }



    @Override
    @Transactional
    public CouponIdResponse updateCoupon(Long couponId, @Nullable List<MultipartFile> couponImages, CouponCreateRequest request) {
        Member member = authService.getLoginMember();

        // 쿠폰을 찾아서 권한 확인
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new CustomApiException(ErrorCode.COUPON_NOT_FOUND));
        if (!coupon.getStore().getMember().equals(member)) {
            throw new CustomApiException(ErrorCode.INVALID_PERMISSION);
        }

        // 쿠폰의 정보를 업데이트
        coupon.updateDetails(request);

        // 새로운 이미지가 제공되었을 경우
        if (couponImages != null && !couponImages.isEmpty()) {
            // 기존 이미지 삭제
            List<CouponImage> existingImages = couponImageRepository.findAllByCoupon(coupon);
            for (CouponImage image : existingImages) {
                couponImageRepository.delete(image);
                s3FileComponent.deleteFile(image.getUrl());
            }

            // 새로운 이미지 업로드 및 저장
            List<CouponImage> newCouponImages = createAndSaveCouponImages(coupon, couponImages);
            coupon.changeImages(newCouponImages);
        }

        // 쿠폰 저장
        couponRepository.save(coupon);

        return new CouponIdResponse(coupon.getId());
    }

    @Override
    public List<CouponSummaryInquiryResponse> inquiryCouponByEachStore(Long storeId) {
        Store store=storeService.loadStore(storeId);
        return inquiryCouponByStore(store).stream()
                .map(couponMapper::toCouponSummaryInquiryResponse)
                .toList();
    }
    public List<Coupon> inquiryCouponByStore(Store store) {
        return couponRepository.findAllByStore(store);
    }


    @Override
    @Transactional(readOnly = true)
    public CouponDetailResponse inquiryCouponDetail(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomApiException(ErrorCode.COUPON_NOT_FOUND));

        return couponMapper.toCouponDetailResponse(coupon);
    }

}
