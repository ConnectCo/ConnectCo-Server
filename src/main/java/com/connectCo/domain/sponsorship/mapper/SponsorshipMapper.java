package com.connectCo.domain.sponsorship.mapper;

import com.connectCo.domain.sponsorship.dto.request.SponsorshipCreateRequest;
import com.connectCo.domain.sponsorship.entity.Sponsorship;
import org.springframework.stereotype.Component;

@Component
public class SponsorshipMapper {

    public Sponsorship toSponsorship(SponsorshipCreateRequest request){

        return Sponsorship.builder()
                .event(request.getEvent())
                .coupon(request.getCoupon())
                .build();
    }

}
