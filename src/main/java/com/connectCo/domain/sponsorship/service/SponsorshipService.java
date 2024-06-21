package com.connectCo.domain.sponsorship.service;


import com.connectCo.domain.sponsorship.dto.request.SponsorshipCreateRequest;
import com.connectCo.domain.sponsorship.dto.response.SponsorshipIdResponse;

public interface SponsorshipService {
    SponsorshipIdResponse createSponsorship(SponsorshipCreateRequest request);
}
