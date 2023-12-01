package com.connectCo.domain.baseentity;

import jakarta.persistence.*;
import lombok.Getter;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Where(clause = "deleted_at is null")
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}