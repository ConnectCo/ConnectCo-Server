package com.connectCo.domain.member.repository;


import com.connectCo.domain.member.entity.LoginType;
import com.connectCo.domain.member.entity.Member;
import com.connectCo.global.exception.CustomApiException;
import com.connectCo.global.exception.ErrorCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getMember(Long id) {
        return findById(id)
            .orElseThrow(() -> new CustomApiException(ErrorCode.USER_NOT_FOUND));
    }

    Optional<Member> findByClientIdAndLoginType(String clientId, LoginType loginType);
}
