package com.connectCo.domain.Member.repository;


import com.connectCo.domain.Member.entity.LoginType;
import com.connectCo.domain.Member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByEmailAndLoginType(String email, LoginType loginType);
}
