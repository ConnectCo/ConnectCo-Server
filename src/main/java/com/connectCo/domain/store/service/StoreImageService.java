package com.connectCo.domain.store.service;

import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreImageService {
    List<StoreImage> createAndSaveStoreImages(Store store, List<MultipartFile> storeImages);
//
//    void deleteExistingImages(List<StoreImage> imagesToRemove);
//
//    void updateStoreImages(Store store, List<String> existingImageUrls, List<MultipartFile> newImages);
}




