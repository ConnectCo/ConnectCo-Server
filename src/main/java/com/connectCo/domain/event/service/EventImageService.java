package com.connectCo.domain.event.service;

import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventImageService {
    List<EventImage> createAndSaveEventImage(Event event, List<MultipartFile> eventImages);
//    void deleteExistingImages(List<EventImage> imagesToRemove);
//    void updateEventImages(Event event, List<String> existingImageUrls, List<MultipartFile> newImages);
}
