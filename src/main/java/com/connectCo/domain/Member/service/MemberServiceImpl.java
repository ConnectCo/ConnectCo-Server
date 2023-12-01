package com.connectCo.domain.Member.service;

import com.connectCo.domain.Member.dto.response.MemberLoginResponse;
import com.connectCo.domain.Member.mapper.MemberMapper;
import com.connectCo.domain.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final AuthService authService;


    @Override
    public MemberLoginResponse saveMemberByNaver(String code, String state) {
        return null;
    }
}
