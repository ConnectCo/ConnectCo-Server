package com.connectCo.domain.store.entity;

import com.connectCo.domain.member.entity.Member;
import com.connectCo.domain.organization.entity.Organization;
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
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_store_like", columnNames = {"organization_id", "store_id"}))
public class StoreLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Store store;

    public boolean changeLike() {
        this.isActive = !this.isActive;
        return isActive;
    }
}
