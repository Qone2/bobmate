package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Coordinate;
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
    public Long savePlace(String name, Coordinate coordinate) {
        Place place1 = Place.createPlace(name, coordinate);
        placeRepository.save(place1);
        return place1.getId();
    }

    public Place findOne(Long placeId) {
        return placeRepository.findOne(placeId);
    }

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    /**
     * 장소 삭제
     */
    @Transactional
    public Long deletePlace(Long placeId) {
        Place place = placeRepository.findOne(placeId);
        place.delete();
        return place.getId();
    }

    /**
     * 장소 이름으로 조회
     * 정규표현식 사용
     */
    public List<Place> findAllByName(String regex) {
        return placeRepository.findAllByName(regex);
    }
}
