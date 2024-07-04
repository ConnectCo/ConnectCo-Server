package com.connectCo.domain.sponsorship.mapper;

import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.repository.EventRepository;
import com.connectCo.domain.sponsorship.dto.request.SponsorshipCreateRequest;
import com.connectCo.domain.sponsorship.entity.Sponsor;
import com.connectCo.domain.sponsorship.entity.Sponsorship;
import org.springframework.stereotype.Component;

@Component
public class SponsorshipMapper {

    EventRepository eventRepository;
    public Sponsorship toSponsorship(Event event, Coupon coupon, Sponsor sponsor){

        return Sponsorship.builder()
                .event(event)
                .coupon(coupon)
                .sponsor(sponsor)
                .build();
    }

}
