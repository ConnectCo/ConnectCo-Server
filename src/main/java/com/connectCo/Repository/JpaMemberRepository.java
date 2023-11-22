package com.connectCo.Repository;

import com.connectCo.domain.member.member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface JpaMemberRepository extends JpaRepository<member, UUID> {
}
