package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    @Transactional
    public Long savePlace(Place place) {
        place.setReviewCount(0);
        placeRepository.save(place);
        return place.getId();
    }

    public Place findOne(Long placeId) {
        return placeRepository.findOne(placeId);
    }

    public List<Place> findAll() {
        return placeRepository.findAll();
    }
}
