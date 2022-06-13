package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.dto.CreateMemberDto;
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
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);
        CreateMemberDto memberDto2 = new CreateMemberDto("member2",
                passwordEncoder.encode("password2"),
                "nickname2", Collections.singletonList("ROLE_USER"));
        Long memberId2 = memberService.join(memberDto2);
        Member member2 = memberService.findOne(memberId2);
        CreateMemberDto memberDto3 = new CreateMemberDto("member3",
                passwordEncoder.encode("password3"),
                "nickname3", Collections.singletonList("ROLE_USER"));
        Long memberId3 = memberService.join(memberDto3);
        Member member3 = memberService.findOne(memberId3);

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
        Long bookmarkId4 = bookmarkService.saveBookmark(member2.getId(), place2.getId());
        Long bookmarkId5 = bookmarkService.saveBookmark(member3.getId(), place3.getId());
        Long bookmarkId6 = bookmarkService.saveBookmark(member3.getId(), place2.getId());

        //when
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId1, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId2, bookmarkId1, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId2, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId3, bookmarkId3, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId3, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId2, bookmarkId2, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId3, bookmarkId4, member2.getId());
        tagBookmarkService.saveTagBookmark(tagId3, bookmarkId5, member3.getId());
        tagBookmarkService.saveTagBookmark(tagId3, bookmarkId6, member3.getId());

        //then
        List<Map.Entry<Long, Integer>> entryList = tagService.findAllByTaggedCount();
        assertTrue(entryList.get(0).getValue() > entryList.get(1).getValue());
        assertEquals(tagId3, entryList.get(0).getKey());
    }

    @Test
    public void 태그내림차순_특정멤버() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);
        CreateMemberDto memberDto2 = new CreateMemberDto("member2",
                passwordEncoder.encode("password2"),
                "nickname2", Collections.singletonList("ROLE_USER"));
        Long memberId2 = memberService.join(memberDto2);
        Member member2 = memberService.findOne(memberId2);
        CreateMemberDto memberDto3 = new CreateMemberDto("member3",
                passwordEncoder.encode("password3"),
                "nickname3", Collections.singletonList("ROLE_USER"));
        Long memberId3 = memberService.join(memberDto3);
        Member member3 = memberService.findOne(memberId3);

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
        Long bookmarkId4 = bookmarkService.saveBookmark(member2.getId(), place2.getId());
        Long bookmarkId5 = bookmarkService.saveBookmark(member3.getId(), place3.getId());
        Long bookmarkId6 = bookmarkService.saveBookmark(member3.getId(), place2.getId());

        //when
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId1, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId2, bookmarkId1, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId2, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId3, bookmarkId3, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId1, bookmarkId3, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId2, bookmarkId2, member1.getId());
        tagBookmarkService.saveTagBookmark(tagId3, bookmarkId4, member2.getId());
        tagBookmarkService.saveTagBookmark(tagId3, bookmarkId5, member3.getId());
        tagBookmarkService.saveTagBookmark(tagId3, bookmarkId6, member3.getId());


        //then
        List<Map.Entry<Long, Integer>> entryList = tagService.findAllByMemberAndTaggedCount(member1.getId());
        assertTrue(entryList.get(0).getValue() > entryList.get(1).getValue());
        assertEquals(tagId1, entryList.get(0).getKey());
    }
}
