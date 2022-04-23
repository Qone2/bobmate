package com.bobmate.bobmate;

import com.bobmate.bobmate.domain.*;
import com.bobmate.bobmate.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class initDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;
        private final PlaceService placeService;
        private final ReviewService reviewService;
        private final MeetService meetService;
        private final PasswordEncoder passwordEncoder;
        private final TagService tagService;
        private final BookmarkService bookmarkService;
        private final TagBookmarkService tagBookmarkService;

        public void dbInit1() {
            Member member1 = new Member();
            member1.setEmail("member1@member.com");
            member1.setPassword(passwordEncoder.encode("password1"));
            member1.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member1);
            Member member2 = new Member();
            member2.setEmail("member2@member.com");
            member2.setPassword(passwordEncoder.encode("password1"));
            member2.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member2);
            Member member3 = new Member();
            member3.setEmail("member3@member.com");
            member3.setPassword(passwordEncoder.encode("password1"));
            member3.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member3);
            Member member4 = new Member();
            member4.setEmail("member4@member.com");
            member4.setPassword(passwordEncoder.encode("password1"));
            member4.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member4);
            Member member5 = new Member();
            member5.setEmail("member5@member.com");
            member5.setPassword(passwordEncoder.encode("password1"));
            member5.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member5);

            Long placeId1 = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
            Long placeId2 = placeService.savePlace("식당2", new Coordinate(13.123, 31.321));
            Long placeId3 = placeService.savePlace("식당3", new Coordinate(12.123, 32.321));

            reviewService.saveReview(member1.getId(), placeId1, "리뷰내용1", 1.5, new ArrayList<>());
            reviewService.saveReview(member1.getId(), placeId1, "리뷰내용2", 2.5, new ArrayList<>());
            reviewService.saveReview(member1.getId(), placeId2, "리뷰내용3", 3.5, new ArrayList<>());
            reviewService.saveReview(member2.getId(), placeId2, "리뷰내용4", 4.5, new ArrayList<>());
            reviewService.saveReview(member3.getId(), placeId3, "리뷰내용5", 3.5, new ArrayList<>());

            Long meetId1 = meetService.saveMeet(member1.getId(), placeId3, "모임1", "링크1");
            meetService.addMember(member4.getId(), meetId1);
            meetService.addMember(member5.getId(), meetId1);


            Long tag0Id = tagService.saveTag("가성비");
            Long tag1Id = tagService.saveTag("카페");
            Long tag2Id = tagService.saveTag("한식");
            Long tag3Id = tagService.saveTag("일식");
            Long tag4Id = tagService.saveTag("양식");
            Long tag5Id = tagService.saveTag("맛있는");
            Long tag6Id = tagService.saveTag("데이트");
            Long tag7Id = tagService.saveTag("친절한");

            Long bookmark0Id = bookmarkService.saveBookmark(member1.getId(), placeId1);

            Long tagBookmark0Id = tagBookmarkService.saveTagBookmark(tag0Id, bookmark0Id, member1.getId());
            Long tagBookmark1Id = tagBookmarkService.saveTagBookmark(tag2Id, bookmark0Id, member1.getId());

        }
    }
}
