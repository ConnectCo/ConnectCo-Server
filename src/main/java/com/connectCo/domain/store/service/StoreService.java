package com.connectCo.domain.store.service;


import com.connectCo.domain.Member.dto.response.MemberInfoResponse;
import com.connectCo.domain.Member.entity.Member;

import java.util.List;

public interface StoreService {

    List<MemberInfoResponse.MyStores> getStoresByMember(Member member);
}
