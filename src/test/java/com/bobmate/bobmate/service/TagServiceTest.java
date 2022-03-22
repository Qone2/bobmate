package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Place;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TagServiceTest {

    @Autowired BookmarkService bookmarkService;
    @Autowired MemberService memberService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired PlaceService placeService;
    @Autowired TagService tagService;
    @Autowired TagBookmarkService tagBookmarkService;


    @Test
    public void 태그내림차순() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member1);

        Place place1 = new Place();
        place1.setName("식당1");
        place1.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place1);

        Place place2 = new Place();
        place2.setName("식당2");
        place2.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place2);

        Place place3 = new Place();
        place3.setName("식당3");
        place3.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place3);

        Long tagId1 = tagService.saveTag("맛있는");
        Long tagId2 = tagService.saveTag("깔끔한");
        Long tagId3 = tagService.saveTag("kind");

        Long bookmarkId1 = bookmarkService.saveBookmark(member1.getId(), place1.getId());
        Long bookmarkId2 = bookmarkService.saveBookmark(member1.getId(), place2.getId());
        Long bookmarkId3 = bookmarkService.saveBookmark(member1.getId(), place3.getId());

        //when
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId1, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId2, bookmarkId1, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId2, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId3, bookmarkId3, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId3, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId2, bookmarkId2, member1.getId());

        //then
        List<Map.Entry<Long, Integer>> entryList = tagService.findAllByTaggedCount();
        assertEquals(3, entryList.get(0).getValue());
        assertEquals(2, entryList.get(1).getValue());
        assertTrue(entryList.get(0).getKey() < entryList.get(1).getKey());
    }
}
