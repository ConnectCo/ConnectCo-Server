package com.connectCo.domain.Member.repository;


import com.connectCo.domain.Member.entity.LoginType;
import com.connectCo.domain.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByClientIdAndLoginType(String clientId, LoginType loginType);
}
