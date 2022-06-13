package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.*;
import com.bobmate.bobmate.dto.CreateMemberDto;
import com.bobmate.bobmate.exception.DeletedPlaceException;
import com.bobmate.bobmate.exception.StarValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewServiceTest {

    @Autowired MemberService memberService;
    @Autowired PlaceService placeService;
    @Autowired ReviewService reviewService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void 리뷰생성() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        String contents = "맛있다!";
        Long reviewId = reviewService.saveReview(memberId1, placeId, contents, 5.0, new ArrayList<>());

        Review review = reviewService.findOne(reviewId);

        //then
        assertEquals(member1, review.getMember());
        assertEquals(place, review.getPlace());
    }

    @Test
    public void 리뷰생성_이상한_별점() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        String contents = "맛있다!";
        Exception e1 = assertThrows(StarValueException.class, () -> reviewService.saveReview(memberId1, placeId, contents, 6.0, new ArrayList<>()));
        Exception e2 = assertThrows(StarValueException.class, () -> reviewService.saveReview(memberId1, placeId, contents, -1.0, new ArrayList<>()));

        //then
        System.out.println(e1.getMessage());
        System.out.println(e2.getMessage());
    }

    @Test
    public void 리뷰생성_부수효과() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        String contents = "맛있다!";
        Long reviewId1 = reviewService.saveReview(memberId1, placeId, contents, 5.0, new ArrayList<>());
        Long reviewId2 = reviewService.saveReview(memberId1, placeId, contents, 4.0, new ArrayList<>());

        //then
        Place place1 = placeService.findOne(placeId);
        assertEquals(2, place1.getReviewCount());
        assertEquals(4.5, place1.getAvgStar());
    }

    @Test
    public void 리뷰삭제_부수효과() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        String contents = "맛있다!";
        Long reviewId1 = reviewService.saveReview(memberId1, placeId, contents, 5.0, new ArrayList<>());
        Long reviewId2 = reviewService.saveReview(memberId1, placeId, contents, 4.0, new ArrayList<>());
        reviewService.deleteReview(reviewId2);

        //then
        Review review1 = reviewService.findOne(reviewId2);
        Place place1 = placeService.findOne(placeId);
        assertEquals(ReviewStatus.DELETED, review1.getReviewStatus());
        assertEquals(1, place1.getReviewCount());
        assertEquals(5, place1.getAvgStar());

        assertEquals(ReviewStatus.DELETED, member1.getReviews().get(1).getReviewStatus());
    }

    @Test
    public void 삭제된장소의리뷰() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        String contents = "맛있다!";
        place.delete();

        //then
        assertThrows(DeletedPlaceException.class, () -> reviewService.saveReview(memberId1, placeId, contents, 5.0, new ArrayList<>()));
    }
}
