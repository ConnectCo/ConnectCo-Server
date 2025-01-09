package com.connectCo.domain.chat.entity;

import com.connectCo.domain.Member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn
    private Member sender; // 메시지 보낸 사람

    private String message;

    private LocalDateTime sendTime;

    public void ChatMessage() {
        this.sendTime = LocalDateTime.now();
    }
}
