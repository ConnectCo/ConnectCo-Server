package com.connectCo.domain.organization.entity;

import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import com.connectCo.domain.store.entity.Store;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
// 같은 대상에 좋아요 행은 하나만 둔다(동시에 두 번 눌러도 중복 행이 생기지 않도록)
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_organization_like", columnNames = {"organization_id", "store_id"}))
public class OrganizationLike extends BaseEntity {

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
