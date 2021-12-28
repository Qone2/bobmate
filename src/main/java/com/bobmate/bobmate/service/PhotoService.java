package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Photo;
import com.bobmate.bobmate.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final PhotoRepository photoRepository;


    public Photo findOne(Long photoId) {
        return photoRepository.findOne(photoId);
    }
}
