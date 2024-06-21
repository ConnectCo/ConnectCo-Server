package com.connectCo.domain.sponsorship.service;


import com.connectCo.domain.Member.entity.Member;
import com.connectCo.domain.Member.service.AuthService;
import com.connectCo.domain.sponsorship.dto.request.SponsorshipCreateRequest;
import com.connectCo.domain.sponsorship.dto.response.SponsorshipIdResponse;
import com.connectCo.domain.sponsorship.entity.Sponsorship;
import com.connectCo.domain.sponsorship.mapper.SponsorshipMapper;
import com.connectCo.domain.sponsorship.repository.SponsorshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SponsorshipServiceImpl implements SponsorshipService{

    private final SponsorshipMapper sponsorshipMapper;
    private final SponsorshipRepository sponsorshipRepository;

    /*
     * 협찬 생성
     */
    @Override
    @Transactional
    public SponsorshipIdResponse createSponsorship(SponsorshipCreateRequest request){
        Sponsorship newSponsorship = createAndSaveSponsorship(request);
        return new SponsorshipIdResponse(newSponsorship.getId());

    }

    private Sponsorship createAndSaveSponsorship(SponsorshipCreateRequest request){
        Sponsorship sponsorship = sponsorshipMapper.toSponsorship(request);
        return sponsorshipRepository.save(sponsorship);
    }

}
