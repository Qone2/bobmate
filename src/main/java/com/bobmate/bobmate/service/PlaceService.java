package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        String[] splitRegex = regex.split(" ");
        if (splitRegex.length == 0) {
            return placeRepository.findAll();
        }

        Set<Place> placesSet = new HashSet<>();
        boolean isFirst = true;

        for (String reg : splitRegex) {
            if (isFirst) {
                placesSet.addAll(placeRepository.findAllByName(reg));
                isFirst = false;
            } else {
                placesSet.retainAll(placeRepository.findAllByName(reg));
            }
        }

        return new ArrayList<>(placesSet);
    }
}
