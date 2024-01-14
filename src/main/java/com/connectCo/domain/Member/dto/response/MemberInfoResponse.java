package com.connectCo.domain.Member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponse {
    private String name;
    private String profileImage;
    private List<String> myStores;
}
