package com.connectCo.domain.store;

import com.connectCo.domain.baseentity.BaseEntity;
import com.connectCo.domain.member.Member;
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
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "store_id" ,columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = true)
    private String storeDescription;

    @Column(nullable = false)
    private String storeNumber;

    @Column(nullable = false)
    private String address;

    @Builder
    public Store(UUID id, com.connectCo.domain.member.Member member, String storeName, String storeDescription, String storeNumber, String address) {
        this.id = id;
        this.member = member;
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.storeNumber = storeNumber;
        this.address = address;
    }
}
