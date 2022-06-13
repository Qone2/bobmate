package com.bobmate.bobmate;

import com.bobmate.bobmate.domain.*;
import com.bobmate.bobmate.dto.CreateMemberDto;
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
            CreateMemberDto memberDto1 = new CreateMemberDto("mmember1",
                    passwordEncoder.encode("password1"),
                    "nnickname1", Collections.singletonList("ROLE_USER"));
            Long memberId1 = memberService.join(memberDto1);
            CreateMemberDto memberDto2 = new CreateMemberDto("mmember2",
                    passwordEncoder.encode("password2"),
                    "nnickname2", Collections.singletonList("ROLE_USER"));
            Long memberId2 = memberService.join(memberDto2);
            CreateMemberDto memberDto3 = new CreateMemberDto("mmember3",
                    passwordEncoder.encode("password3"),
                    "nnickname3", Collections.singletonList("ROLE_USER"));
            Long memberId3 = memberService.join(memberDto3);
            CreateMemberDto memberDto4 = new CreateMemberDto("mmember4",
                    passwordEncoder.encode("password4"),
                    "nnickname4", Collections.singletonList("ROLE_USER"));
            Long memberId4 = memberService.join(memberDto4);
            CreateMemberDto memberDto5 = new CreateMemberDto("mmember5",
                    passwordEncoder.encode("password5"),
                    "nnickname5", Collections.singletonList("ROLE_USER"));
            Long memberId5 = memberService.join(memberDto5);

            Long placeId1 = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
            Long placeId2 = placeService.savePlace("식당2", new Coordinate(13.123, 31.321));
            Long placeId3 = placeService.savePlace("식당3", new Coordinate(12.123, 32.321));

            reviewService.saveReview(memberId1, placeId1, "리뷰내용1", 1.5, new ArrayList<>());
            reviewService.saveReview(memberId1, placeId1, "리뷰내용2", 2.5, new ArrayList<>());
            reviewService.saveReview(memberId1, placeId2, "리뷰내용3", 3.5, new ArrayList<>());
            reviewService.saveReview(memberId2, placeId2, "리뷰내용4", 4.5, new ArrayList<>());
            reviewService.saveReview(memberId3, placeId3, "리뷰내용5", 3.5, new ArrayList<>());

            Long meetId1 = meetService.saveMeet(memberId1, placeId3, "모임1", "링크1");
            meetService.addMember(memberId4, meetId1);
            meetService.addMember(memberId5, meetId1);


            Long tag0Id = tagService.saveTag("가성비");
            Long tag1Id = tagService.saveTag("카페");
            Long tag2Id = tagService.saveTag("한식");
            Long tag3Id = tagService.saveTag("일식");
            Long tag4Id = tagService.saveTag("양식");
            Long tag5Id = tagService.saveTag("맛있는");
            Long tag6Id = tagService.saveTag("데이트");
            Long tag7Id = tagService.saveTag("친절한");

            Long bookmark0Id = bookmarkService.saveBookmark(memberId1, placeId1);

            Long tagBookmark0Id = tagBookmarkService.saveTagBookmark(tag0Id, bookmark0Id, memberId1);
            Long tagBookmark1Id = tagBookmarkService.saveTagBookmark(tag2Id, bookmark0Id, memberId1);

            // 배포 테스트용

        }
    }
}
