package com.connectCo.domain.group;

import com.connectCo.domain.baseentity.BaseEntity;
import com.connectCo.domain.enums.GroupType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Where(clause = "deleted_at is null")
public class Group extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private GroupType group_type;

    @Builder
    public Group(UUID id, String groupName, String address, GroupType group_type) {
        this.id = id;
        this.groupName = groupName;
        this.address = address;
        this.group_type = group_type;
    }
}
