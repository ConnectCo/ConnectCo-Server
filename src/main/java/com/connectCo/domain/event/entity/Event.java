package com.connectCo.domain.event.entity;

import com.connectCo.global.utils.BaseEntity;
import com.connectCo.domain.organization.entity.Organization;
import com.connectCo.domain.Member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Organization group;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @JoinColumn
    @ElementCollection(fetch = FetchType.LAZY)
    private List<FoodCategory> foodCategories = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;
}
