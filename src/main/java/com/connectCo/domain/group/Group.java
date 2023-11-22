package com.connectCo.domain.group;

import com.connectCo.domain.BaseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Group extends baseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    private UUID id;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String group_type;

}
