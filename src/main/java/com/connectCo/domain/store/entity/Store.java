package com.connectCo.domain.store.entity;

import com.connectCo.domain.Member.entity.Member;
import com.connectCo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "deleted_at is null")
public class Store extends BaseEntity {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String storeNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String operatingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @OneToMany(mappedBy = "store")
    private List<StoreImage> images = new ArrayList<>();

    public void changeImages(List<StoreImage> storeImages) {
        // 기존 이미지가 있다면 삭제
        if(this.images != null) removeImages();

        // 새로운 이미지로 변경
        this.images = storeImages;
    }

    private void removeImages() {
        this.images.forEach(BaseEntity::delete);
    }
}
