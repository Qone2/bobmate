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

    public void save(Place place) {
        em.persist(place);
    }

    public Place findOne(Long placeId) {
        return em.find(Place.class, placeId);
    }

    public List<Place> findAll() {
        return em.createQuery("select p from Place p", Place.class)
                .getResultList();
    }
}
