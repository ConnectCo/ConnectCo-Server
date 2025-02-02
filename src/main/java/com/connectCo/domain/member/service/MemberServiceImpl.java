package com.connectCo.domain.member.service;

import com.connectCo.domain.member.mapper.MemberMapper;
import com.connectCo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    private final AuthService authService;
    private final StoreService storeService;
}
