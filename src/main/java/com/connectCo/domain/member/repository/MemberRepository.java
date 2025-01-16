package com.connectCo.domain.member.repository;


import com.connectCo.domain.member.entity.LoginType;
import com.connectCo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByClientIdAndLoginType(String clientId, LoginType loginType);
    Optional<Member> findById(Long id);
}
