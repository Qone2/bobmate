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

    /**
     * 장소 등록
     */
    @Transactional
    public Long savePlace(Place place) {
        Place place1 = Place.createPlace(place.getName(), place.getCoordinate());
        placeRepository.save(place1);
        return place1.getId();
    }

    public Place findOne(Long placeId) {
        return placeRepository.findOne(placeId);
    }

    public List<Place> findAll() {
        return placeRepository.findAll();
    }
}
