package com.connectCo.service;


import com.connectCo.Repository.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final JpaMemberRepository jpaMemberRepository;


}
