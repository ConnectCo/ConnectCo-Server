package com.connectCo.domain.chat.repository;

import com.connectCo.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
