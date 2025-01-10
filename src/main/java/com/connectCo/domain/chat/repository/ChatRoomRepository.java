package com.connectCo.domain.chat.repository;

import com.connectCo.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.connectCo.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("""
        SELECT cr
        FROM ChatRoom cr
        WHERE cr.sender.id = :userId OR cr.receiver.id = :userId
        ORDER BY (SELECT MAX(c.createdAt) FROM Chat c WHERE c.chatRoom.id = cr.id) DESC
    """)
    List<ChatRoom> findChatRoomsByMember(@Param("userId") Long userId);
}
