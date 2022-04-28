package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlaceRepository {

    private final EntityManager em;

    /**
     * 장소 저장
     */
    public void save(Place place) {
        em.persist(place);
    }

    /**
     * 장소 단일 조회
     */
    public Place findOne(Long placeId) {
        return em.find(Place.class, placeId);
    }

    /**
     * 장소 전체 조회
     */
    public List<Place> findAll() {
        return em.createQuery("select p from Place p", Place.class)
                .getResultList();
    }
}
