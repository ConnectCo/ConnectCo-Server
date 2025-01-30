package com.connectCo.domain.chat.entity;


import com.connectCo.domain.Member.entity.Member;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
