package com.connectCo.domain.member.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponse {
    private String name;
    private String profileImage;
    private List<MyStores> myStores;

    @Getter
    @Builder
    public static class MyStores {
        private Long storeId;
        private String name;
    }
}
