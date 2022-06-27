package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Place;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaceServiceTest {
    @Autowired
    PlaceService placeService;
    
    @Test
    public void 정규표현식검색() throws Exception {
        //given
        Long placeId1 = placeService.savePlace("t미쓰 와이프", new Coordinate(123.1, 123.2));
        Place place1 = placeService.findOne(placeId1);
        Long placeId2 = placeService.savePlace("t트와일라잇", new Coordinate(123.1, 123.2));
        Place place2 = placeService.findOne(placeId2);
        Long placeId3 = placeService.savePlace("t식당3", new Coordinate(123.1, 123.2));
        Place place3 = placeService.findOne(placeId3);
        
        //when
        List<Place> placeList1 = placeService.findAllByName("와[이-잏]");
        List<Place> placeList2 = placeService.findAllByName("[a-z]");
        List<Place> placeList3 = placeService.findAllByName("^[a-z]*$");
        
        
        //then
        assertEquals(2, placeList1.size());
        assertEquals(3, placeList2.size());
        assertEquals(0, placeList3.size());
    }
}
