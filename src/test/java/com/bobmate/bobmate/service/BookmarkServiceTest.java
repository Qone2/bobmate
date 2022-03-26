package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.exception.BookmarkDuplicateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookmarkServiceTest {

    @Autowired BookmarkService bookmarkService;
    @Autowired MemberService memberService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired PlaceService placeService;

    @Test
    public void 북마크생성() throws Exception {
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

        //when
        bookmarkService.saveBookmark(member1.getId(), place.getId());

        //then
        assertEquals(place, member1.getBookmarks().get(0).getPlace());

    }


    @Test
    public void 북마크삭제() throws Exception {
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

        //when
        bookmarkService.saveBookmark(member1.getId(), place.getId());
        bookmarkService.deleteBookmark(member1.getId(), place.getId());
        Member findMember = memberService.findOne(member1.getId());

        //then
        assertEquals(0, findMember.getBookmarks().size());

    }


    @Test
    public void 북마크중복() throws Exception {
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

        //when
        bookmarkService.saveBookmark(member1.getId(), place.getId());

        //then
        assertThrows(BookmarkDuplicateException.class, () -> bookmarkService.saveBookmark(member1.getId(), place.getId()));

    }

}
