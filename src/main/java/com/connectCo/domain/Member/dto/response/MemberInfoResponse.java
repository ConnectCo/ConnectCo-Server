package com.connectCo.domain.Member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberInfoResponse {
    private String name;
    private String profileImage;
    private List<String> myStores;
}
