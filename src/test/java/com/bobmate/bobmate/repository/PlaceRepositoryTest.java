package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Place;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaceRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    PlaceRepository placeRepository;

    @Test
    public void 쿼리테스트() throws Exception {
        //given
        Place place = Place.createPlace("t미쓰 와이프", new Coordinate(123.1, 231.1));
        em.persist(place);

        //when
        List<Place> placeList = placeRepository.findAllByName("와[이-잏]");


        //then
        assertEquals("t미쓰 와이프", placeList.get(0).getName());
    }
}
