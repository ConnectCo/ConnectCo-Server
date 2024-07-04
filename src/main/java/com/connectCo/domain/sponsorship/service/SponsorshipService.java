package com.connectCo.domain.sponsorship.service;


import com.connectCo.domain.sponsorship.dto.request.SponsorshipCreateRequest;
import com.connectCo.domain.sponsorship.dto.request.SponsorshipDecisionRequest;
import com.connectCo.domain.sponsorship.dto.response.SponsorshipIdResponse;

public interface SponsorshipService {
    SponsorshipIdResponse createSponsorshipByStore(SponsorshipCreateRequest request);

    SponsorshipIdResponse createSponsorshipByMember(SponsorshipCreateRequest request);

    SponsorshipIdResponse decisionSponsorship(SponsorshipDecisionRequest request);
}
