package com.connectCo.domain.chat.repository;

import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.member.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("""
        SELECT cr
        FROM ChatRoom cr
        WHERE cr.sender.id = :profileId OR cr.receiver.id = : profileId
        ORDER BY (SELECT MAX(c.createdAt) FROM Chat c WHERE c.chatRoom.id = cr.id) DESC
    """)
    List<ChatRoom> findChatRoomsByProfile(@Param("profileId") Long profileId);

    Optional<ChatRoom> findBySenderAndReceiver(Profile sender, Profile receiver);
}
