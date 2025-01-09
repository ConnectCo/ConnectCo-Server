package com.connectCo.domain.chat.repository;

import com.connectCo.domain.chat.dto.response.ChatResponse;
import com.connectCo.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
        SELECT c
        FROM Chat c
        WHERE c.chatRoom.id = :chatRoomId
        ORDER BY c.createdAt ASC
    """)
    List<ChatResponse> findChatsByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
