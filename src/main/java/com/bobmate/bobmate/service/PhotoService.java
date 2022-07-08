package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Photo;
import com.bobmate.bobmate.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final PhotoRepository photoRepository;


    /**
     * 사진 단일 조회
     */
    public Photo findOne(Long photoId) {
        return photoRepository.findOne(photoId);
    }

    /**
     * 사진 전체 조회
     */
    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    /**
     * 사진 삭제
     */
    public void deletePhoto(Long photoId) {
        Photo photo = photoRepository.findOne(photoId);
        photoRepository.delete(photo);
    }
}
