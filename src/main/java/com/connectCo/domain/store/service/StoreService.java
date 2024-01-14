package com.connectCo.domain.store.service;


import com.connectCo.domain.Member.entity.Member;

import java.util.List;

public interface StoreService {

    List<String> getStoresByMember(Member member);
}
