package com.connectCo.domain.store;

import com.connectCo.domain.BaseEntity.baseEntity;
import com.connectCo.domain.member.member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class store extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private member Member;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = true)
    private String storeDescription;

    @Column(nullable = false)
    private String storeNumber;

    @Column(nullable = false)
    private String address;


}
