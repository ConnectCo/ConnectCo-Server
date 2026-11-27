package com.connectCo.domain.chat.repository;

import com.connectCo.domain.chat.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    // 가장 최근 메시지부터 size+1개.
    // 별도 인덱스는 만들지 않았다: InnoDB 보조 인덱스는 PK를 포함하므로 FK 인덱스(chat_room_id)가 곧 (chat_room_id, id)이고,
    // 이 인덱스를 역순으로 size+1개만 읽고 멈춘다(정렬 없음).
    @Query("SELECT c FROM Chat c WHERE c.chatRoom.id = :chatRoomId ORDER BY c.id DESC")
    List<Chat> findLatest(@Param("chatRoomId") Long chatRoomId, Pageable pageable);

    // 커서(이전 응답에서 가장 오래된 메시지 ID)보다 오래된 메시지부터 size+1개
    @Query("SELECT c FROM Chat c WHERE c.chatRoom.id = :chatRoomId AND c.id < :cursor ORDER BY c.id DESC")
    List<Chat> findBefore(@Param("chatRoomId") Long chatRoomId, @Param("cursor") Long cursor, Pageable pageable);
}
