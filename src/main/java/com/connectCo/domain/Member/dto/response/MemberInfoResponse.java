package com.connectCo.domain.Member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MemberInfoResponse {
    private String name;
    private String profileImage;
    private List<MyStores> myStores;

    @Getter
    @Builder
    public static class MyStores {
        private UUID storeId;
        private String name;
    }
}
