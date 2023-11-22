package com.connectCo.domain.event;

import com.connectCo.domain.BaseEntity.baseEntity;
import com.connectCo.domain.enums.category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class event extends baseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private UUID id;

    @Column(nullable = false)
    String name;

    @Enumerated(EnumType.STRING)
    private category category;
}
