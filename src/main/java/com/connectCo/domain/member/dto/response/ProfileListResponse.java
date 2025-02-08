package com.connectCo.domain.member.dto.response;

import com.connectCo.domain.member.entity.ProfileType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileListResponse {
    private List<ProfileResponse> storeProfiles;
    private List<ProfileResponse> organizationProfiles;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProfileResponse {
        private Long profileId;
        private ProfileType profileType;
        private String profileName;
        private String profileImageUrl;
    }
}
