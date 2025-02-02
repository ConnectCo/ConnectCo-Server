package com.connectCo.domain.member.repository;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.domain.member.entity.ProfileType;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    default Profile getProfile(Long profileId, ProfileType profileType) {
        return findByIdAndProfileType(profileId, profileType)
            .orElseThrow(() -> new CustomApiException(ErrorCode.PROFILE_NOT_FOUND));
    }

    Optional<Profile> findByIdAndProfileType(Long id, ProfileType profileType);
    List<Profile> findByMember(Member member);
}
