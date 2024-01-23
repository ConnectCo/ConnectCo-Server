package com.connectCo.domain.coupon.service;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
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
import com.connectCo.domain.store.dto.request.StoreCreateRequest;
import com.connectCo.domain.store.dto.response.StoreIdResponse;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import com.connectCo.domain.store.repository.StoreRepository;
import com.connectCo.domain.store.service.StoreService;
import com.connectCo.utils.S3FileComponent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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

        Store store = storeService.findById(request.getStoreId());
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

}
