package com.connectCo.domain.event.service;

import com.connectCo.domain.event.entity.Event;
import com.connectCo.domain.event.entity.EventImage;
import com.connectCo.domain.event.mapper.EventMapper;
import com.connectCo.domain.event.repository.EventImageRepository;
import com.connectCo.utils.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventImageServiceImpl implements EventImageService {

    private final EventImageRepository eventImageRepository;
    private final S3FileComponent s3FileComponent;
    private final EventMapper eventMapper;

    /*
     * 이벤트 이미지 객체를 생성하고 DB에 저장
     */
    @Override
    public List<EventImage> createAndSaveEventImage(Event event, List<MultipartFile> eventImages) {
        return eventImages.stream()
                .map(eventImage -> s3FileComponent.uploadFile("event", eventImage))
                .map(eventUrl -> eventMapper.toEventImage(event, eventUrl))
                .map(eventImageRepository::save)
                .toList();
    }

    /*
     * 삭제할 기존 이미지를 S3와 DB에서 삭제
     */
    @Override
    public void deleteExistingImages(List<EventImage> imagesToRemove) {
        for (EventImage image : imagesToRemove) {
            s3FileComponent.deleteFile(image.getUrl());
            eventImageRepository.delete(image);
        }
    }

    /*
     * 이벤트 이미지를 업데이트
     */
    @Override
    public void updateEventImages(Event event, List<String> existingImageUrls, List<MultipartFile> newImages) {
        List<EventImage> existingImages = event.getImages();
        // 유지할 기존 이미지
        List<EventImage> existingImagesToKeep = existingImages.stream()
                .filter(image -> existingImageUrls.contains(image.getUrl()))
                .collect(Collectors.toList());
        // 삭제할 기존 이미지
        List<EventImage> existingImagesToRemove = existingImages.stream()
                .filter(image -> !existingImageUrls.contains(image.getUrl()))
                .toList();

        // 새로운 이미지 추가
        List<EventImage> newEventImages = (newImages != null)
                ? createAndSaveEventImage(event, newImages) : List.of();

        // 유지할 기존 이미지와 새로운 이미지 병합 후 Event에 할당
        existingImagesToKeep.addAll(newEventImages);
        event.changeImages(existingImagesToKeep);

        // 삭제할 기존 이미지 삭제
        deleteExistingImages(existingImagesToRemove);
    }
}
