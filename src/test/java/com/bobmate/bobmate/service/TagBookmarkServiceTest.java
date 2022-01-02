package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TagBookmarkServiceTest {

    @Autowired BookmarkService bookmarkService;
    @Autowired MemberService memberService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired PlaceService placeService;
    @Autowired TagService tagService;
    @Autowired TagBookmarkService tagBookmarkService;

    @Test
    public void 태그하기() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member1);

        Place place = new Place();
        place.setName("식당0");
        place.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place);

        Long tagId1 = tagService.saveTag("맛있는");
        Long tagId2 = tagService.saveTag("깔끔한");

        Long bookmarkId1 = bookmarkService.saveBookmark(member1.getId(), place.getId());

        //when
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId1, member1.getId());

        //then
        assertEquals(1, member1.getTagBookmarks().size());
        assertEquals(bookmarkId1, member1.getTagBookmarks().get(0).getBookmark().getId());

    }

    @Test
    public void 태그교집합() throws Exception {
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

        List<Bookmark> taggedBookmark1 = tagBookmarkService.findTaggedBookmark(member1.getId(), new ArrayList<>(Arrays.asList(tagId1, tagId2)));
        List<Bookmark> taggedBookmark2 = tagBookmarkService.findTaggedBookmark(member1.getId(), new ArrayList<>(Arrays.asList(tagId1)));
        List<Bookmark> taggedBookmark3 = tagBookmarkService.findTaggedBookmark(member1.getId(), new ArrayList<>(Arrays.asList(tagId1, tagId2, tagId3)));

        //then
        assertEquals(1, taggedBookmark1.size());
        assertEquals(2, taggedBookmark2.size());
        assertEquals(bookmarkId1, taggedBookmark1.get(0).getId());
        assertEquals(0, taggedBookmark3.size());

    }

    @Test
    public void 태그삭제() throws Exception {
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

        tagBookmarkService.deleteTagBookmark(tagId1, bookmarkId1, member1.getId());

        List<Bookmark> taggedBookmark1 = tagBookmarkService.findTaggedBookmark(member1.getId(), new ArrayList<>(Arrays.asList(tagId1, tagId2)));
        List<Bookmark> taggedBookmark2 = tagBookmarkService.findTaggedBookmark(member1.getId(), new ArrayList<>(Arrays.asList(tagId1)));
        List<Bookmark> taggedBookmark3 = tagBookmarkService.findTaggedBookmark(member1.getId(), new ArrayList<>(Arrays.asList(tagId1, tagId2, tagId3)));

        Member findMember = memberService.findOne(member1.getId());

        //then
        assertEquals(0, taggedBookmark3.size());
        assertEquals(0, taggedBookmark1.size());
        assertEquals(1, taggedBookmark2.size());

        assertEquals(3, findMember.getTagBookmarks().size());
    }
}
