package com.connectCo.domain.chat.repository;

import com.connectCo.domain.chat.entity.ChatRoom;
import com.connectCo.domain.member.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 기존: 방마다 chat 테이블에서 MAX(created_at)을 다시 구해 정렬했다(메시지가 쌓일수록 느려짐).
    // 메시지를 보낼 때 갱신하는 recent_message_time으로 정렬하고, 상대 프로필을 함께 가져와 방마다 추가 조회하지 않는다.
    @Query("""
        SELECT cr
        FROM ChatRoom cr
        JOIN FETCH cr.sender
        JOIN FETCH cr.receiver
        WHERE cr.sender.id = :profileId OR cr.receiver.id = :profileId
        ORDER BY cr.recentMessageTime DESC, cr.id DESC
    """)
    List<ChatRoom> findChatRoomsByProfile(@Param("profileId") Long profileId);

    // 누가 먼저 대화를 시작했는지와 상관없이 두 프로필의 방을 찾는다.
    @Query("""
        SELECT cr
        FROM ChatRoom cr
        WHERE (cr.sender = :a AND cr.receiver = :b) OR (cr.sender = :b AND cr.receiver = :a)
    """)
    List<ChatRoom> findBetween(@Param("a") Profile a, @Param("b") Profile b);

    default Optional<ChatRoom> findRoomBetween(Profile a, Profile b) {
        return findBetween(a, b).stream().findFirst();
    }
}
