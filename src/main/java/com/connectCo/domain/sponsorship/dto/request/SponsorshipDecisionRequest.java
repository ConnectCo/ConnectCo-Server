package com.connectCo.domain.sponsorship.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SponsorshipDecisionRequest {

    private Long eventId;

    private Long couponId;

    private Boolean isAccept;

}
