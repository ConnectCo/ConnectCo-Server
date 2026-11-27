package com.connectCo.domain.event.entity;

import com.connectCo.domain.store.entity.Store;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
// 같은 대상에 좋아요 행은 하나만 둔다(동시에 두 번 눌러도 중복 행이 생기지 않도록)
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_event_like", columnNames = {"store_id", "event_id"}))
public class EventLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Event event;

    public boolean changeLike(){
        this.isActive = !this.isActive;
        return isActive;
    }

}
