package com.connectCo.domain.sponsorship.service;


import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.coupon.repository.CouponRepository;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.repository.EventRepository;
import com.connectCo.domain.sponsorship.dto.request.SponsorshipCreateRequest;
import com.connectCo.domain.sponsorship.dto.request.SponsorshipDecisionRequest;
import com.connectCo.domain.sponsorship.dto.response.SponsorshipIdResponse;
import com.connectCo.domain.sponsorship.entity.Sponsorship;
import com.connectCo.domain.sponsorship.mapper.SponsorshipMapper;
import com.connectCo.domain.sponsorship.repository.SponsorshipRepository;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SponsorshipServiceImpl implements SponsorshipService{
    private final SponsorshipMapper sponsorshipMapper;
    private final SponsorshipRepository sponsorshipRepository;
    private final EventRepository eventRepository;
    private final CouponRepository couponRepository;
    /*
     * 협찬 생성
     */
    @Override
    @Transactional
    public SponsorshipIdResponse createSponsorship(SponsorshipCreateRequest request){
        Event event = eventRepository.findById(request.getEventId()).orElseThrow(() -> new CustomApiException(ErrorCode.EVENT_NOT_FOUND));
        Coupon coupon = couponRepository.findById(request.getCouponId()).orElseThrow(() -> new CustomApiException(ErrorCode.COUPON_NOT_FOUND));
        Sponsorship newSponsorship = createAndSaveSponsorship(event, coupon);
        return new SponsorshipIdResponse(newSponsorship.getId());

    }

    private Sponsorship createAndSaveSponsorship(Event event, Coupon coupon){
        Sponsorship sponsorship = sponsorshipMapper.toSponsorship(event, coupon);
        return sponsorshipRepository.save(sponsorship);
    }

    /*
     * 협찬 수락 or 거절
     */
    @Override
    @Transactional
    public SponsorshipIdResponse decisionSponsorship(SponsorshipDecisionRequest request){

        Long eventId = request.getEventId();
        Long couponId = request.getCouponId();

        Sponsorship sponsorship = sponsorshipRepository.findByEventIdAndCouponId(eventId, couponId);

        if(request.getIsAccept()){
            sponsorship.updateIsComplete();
            //수락 시 협찬 isComplete = true 로 변경
        }

        else {
            sponsorship.delete();
            // 거절 시 협찬 소프트 delete
        }

        return new SponsorshipIdResponse(sponsorship.getId());
    }
}
