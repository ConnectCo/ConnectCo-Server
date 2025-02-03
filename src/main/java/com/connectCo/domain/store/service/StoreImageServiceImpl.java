package com.connectCo.domain.store.service;

import com.connectCo.domain.store.entity.Store;
import com.connectCo.domain.store.entity.StoreImage;
import com.connectCo.domain.store.mapper.StoreMapper;
import com.connectCo.domain.store.repository.StoreImageRepository;
import com.connectCo.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreImageServiceImpl implements StoreImageService {

    private final StoreImageRepository storeImageRepository;
    private final S3FileComponent s3FileComponent;
    private final StoreMapper storeMapper;

    /*
     * 가게 이미지 객체를 생성하고 DB에 저장
     */
    @Override
    public List<StoreImage> createAndSaveStoreImages(Store store, List<MultipartFile> storeImages) {
        return storeImages.stream()
                .map(storeImage -> s3FileComponent.uploadFile("store", storeImage))
                .map(storeUrl -> storeMapper.toStoreImage(store, storeUrl))
                .map(storeImageRepository::save)
                .toList();
    }

    /*
     * 삭제할 기존 이미지를 S3와 DB에서 삭제
     */
    @Override
    @Transactional
    public void deleteImages(Store store) {
        List<StoreImage> imagesToRemove = storeImageRepository.findAllByStore(store);
        deleteExistingImages(imagesToRemove);
    }

    /*
     * 기존 이미지를 S3와 DB에서 삭제
     */
    @Override
    @Transactional
    public void deleteExistingImages(List<StoreImage> imagesToRemove) {
        for (StoreImage image : imagesToRemove) {
            s3FileComponent.deleteFile(image.getUrl());
            storeImageRepository.delete(image);
        }
    }

    /*
     * 가게 이미지를 업데이트
     */
    @Override
    @Transactional
    public String updateStoreImages(Store store, List<String> existingImageUrls, List<MultipartFile> newImages) {
        // 기존 이미지를 유지하거나 삭제
        List<StoreImage> existingImages = storeImageRepository.findAllByStore(store);
        List<StoreImage> existingImagesToKeep = existingImages.stream()
                .filter(image -> existingImageUrls.contains(image.getUrl()))
                .collect(Collectors.toList());

        List<StoreImage> imagesToRemove = existingImages.stream()
                .filter(image -> !existingImageUrls.contains(image.getUrl()))
                .toList();

        // 새로운 이미지 추가
        List<StoreImage> newStoreImages = (newImages != null)
                ? createAndSaveStoreImages(store, newImages) : List.of();

        // 기존 이미지와 새로운 이미지를 합침
        existingImagesToKeep.addAll(newStoreImages);

        // 삭제할 기존 이미지 삭제
        deleteExistingImages(imagesToRemove);

        return existingImagesToKeep.get(0).getUrl();
    }
}
