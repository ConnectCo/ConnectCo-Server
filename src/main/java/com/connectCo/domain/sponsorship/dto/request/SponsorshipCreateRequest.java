package com.connectCo.domain.sponsorship.dto.request;


import com.connectCo.domain.coupon.entity.Coupon;
import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SponsorshipCreateRequest {

    private Event event;

    private Coupon coupon;

}
