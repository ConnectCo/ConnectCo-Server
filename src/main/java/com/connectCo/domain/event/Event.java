package com.connectCo.domain.event;

import com.connectCo.domain.baseentity.BaseEntity;
import com.connectCo.domain.enums.Category;
import com.connectCo.domain.group.Group;
import com.connectCo.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Where(clause = "deleted_at is null")
public class Event extends BaseEntity
{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "event_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Builder
    public Event(UUID id, Member member, Group group, String name, Category category, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.member = member;
        this.group = group;
        this.name = name;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
