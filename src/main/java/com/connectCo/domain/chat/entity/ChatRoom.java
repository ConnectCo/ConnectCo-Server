package com.connectCo.domain.chat.entity;


import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.member.entity.Profile;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recentMessage;

    private LocalDateTime recentMessageTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Profile sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Profile receiver;

    // 메시지 조회에는 쓰지 않는다(방의 메시지 전체를 불러오게 되므로). 채팅방 삭제 시 함께 지우는 용도.
    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    public boolean isParticipant(Long profileId) {
        return sender.getId().equals(profileId) || receiver.getId().equals(profileId);
    }

    public Profile getOther(Long profileId) {
        return sender.getId().equals(profileId) ? receiver : sender;
    }

    public void updateRecentMessage(String recentMessage, LocalDateTime recentMessageTime) {
        this.recentMessage = recentMessage;
        this.recentMessageTime = recentMessageTime;
    }

}
