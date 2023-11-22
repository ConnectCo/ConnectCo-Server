package com.connectCo.domain.storeimage;

import com.connectCo.domain.store.store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class storeimage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_image_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private store Store;
}
