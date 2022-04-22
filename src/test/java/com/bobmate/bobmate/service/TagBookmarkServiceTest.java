package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.*;
import com.bobmate.bobmate.exception.TagBookmarkDuplicateException;
import com.bobmate.bobmate.exception.TagBookmarkMemberException;
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

    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PlaceService placeService;
    @Autowired
    TagService tagService;
    @Autowired
    TagBookmarkService tagBookmarkService;

    @Test
    public void 태그하기() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member1);

        Long placeId = placeService.savePlace("식당0", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        Long tagId1 = tagService.saveTag("good");
        Long tagId2 = tagService.saveTag("clean");

        Long bookmarkId1 = bookmarkService.saveBookmark(member1.getId(), place.getId());

        //when
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId1, member1.getId());

        //then
        assertEquals(1, tagBookmarkService.findAllByMemberId(member1.getId()).size());
        assertEquals(bookmarkId1, tagBookmarkService.findAllByMemberId(member1.getId()).get(0).getBookmark().getId());

    }

    @Test
    public void 태그교집합() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member1);

        Long placeId1 = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place1 = placeService.findOne(placeId1);

        Long placeId2 = placeService.savePlace("식당2", new Coordinate(123.123, 321.321));
        Place place2 = placeService.findOne(placeId2);

        Long placeId3 = placeService.savePlace("식당3", new Coordinate(123.123, 321.321));
        Place place3 = placeService.findOne(placeId3);

        Long tagId1 = tagService.saveTag("good");
        Long tagId2 = tagService.saveTag("clean");
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

        Long placeId1 = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place1 = placeService.findOne(placeId1);

        Long placeId2 = placeService.savePlace("식당2", new Coordinate(123.123, 321.321));
        Place place2 = placeService.findOne(placeId2);

        Long placeId3 = placeService.savePlace("식당3", new Coordinate(123.123, 321.321));
        Place place3 = placeService.findOne(placeId3);

        Long tagId1 = tagService.saveTag("good");
        Long tagId2 = tagService.saveTag("clean");
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

        assertEquals(3, tagBookmarkService.findAllByMemberId(findMember.getId()).size());
    }

    @Test
    public void 태그중복() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member1);

        Long placeId = placeService.savePlace("식당0", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        Long tagId1 = tagService.saveTag("good");
        Long tagId2 = tagService.saveTag("clean");

        Long bookmarkId1 = bookmarkService.saveBookmark(member1.getId(), place.getId());

        //when
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId1, member1.getId());

        //then
        assertThrows(TagBookmarkDuplicateException.class, () -> tagBookmarkService.saveTagBookmark(tagId1, bookmarkId1, member1.getId()));

    }

    @Test
    public void 태그북마크멤버불일치() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member1);
        Member member2 = new Member();
        member2.setEmail("member2@member1.com");
        member2.setPassword(passwordEncoder.encode("password1"));
        member2.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member2);

        Long placeId = placeService.savePlace("식당0", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        Long tagId1 = tagService.saveTag("good");
        Long tagId2 = tagService.saveTag("clean");

        //when
        Long bookmarkId1 = bookmarkService.saveBookmark(member1.getId(), place.getId());
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId1, member1.getId());

        //then
        assertThrows(TagBookmarkMemberException.class, () -> tagBookmarkService.saveTagBookmark(tagId1, bookmarkId1, member2.getId()));

    }
}
