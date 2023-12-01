package com.connectCo.domain.storeimage;

import com.connectCo.domain.image.Image;
import com.connectCo.domain.store.Store;
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
public class StoreImage {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="image_id", nullable = false)
    private Image image;

    @Builder
    public StoreImage(UUID id, Store store, Image image){
        this.id = id;
        this.store = store;
        this.image = image;
    }

}
