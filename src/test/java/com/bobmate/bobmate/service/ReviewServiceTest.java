package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.*;
import com.bobmate.bobmate.exception.StarValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        Long memberId = memberService.join(member1);

        Place place = new Place();
        place.setName("식당1");
        Coordinate coordinate = new Coordinate(123.1, 321.3);
        place.setCoordinate(coordinate);
        Long placeId = placeService.savePlace(place);

        //when
        String contents = "맛있다!";
        Long reviewId = reviewService.saveReview(memberId, placeId, contents, 5.0);

        Review review = reviewService.findOne(reviewId);

        //then
        assertEquals(member1, review.getMember());
        assertEquals(place, review.getPlace());
    }

    @Test
    public void 리뷰생성_이상한_별점() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        Long memberId = memberService.join(member1);

        Place place = new Place();
        place.setName("식당1");
        Coordinate coordinate = new Coordinate(123.1, 321.3);
        place.setCoordinate(coordinate);
        Long placeId = placeService.savePlace(place);

        //when
        String contents = "맛있다!";
        Exception e1 = assertThrows(StarValueException.class, () -> reviewService.saveReview(memberId, placeId, contents, 6.0));
        Exception e2 = assertThrows(StarValueException.class, () -> reviewService.saveReview(memberId, placeId, contents, -1.0));

        //then
        System.out.println(e1.getMessage());
        System.out.println(e2.getMessage());
    }

    @Test
    public void 리뷰생성_부수효과() throws Exception {
        //given
        Member member = new Member();
        member.setEmail("회원1");
        Long memberId = memberService.join(member);

        Place place = new Place();
        place.setName("식당1");
        Coordinate coordinate = new Coordinate(123.1, 321.3);
        place.setCoordinate(coordinate);
        Long placeId = placeService.savePlace(place);

        //when
        String contents = "맛있다!";
        Long reviewId1 = reviewService.saveReview(memberId, placeId, contents, 5.0);
        Long reviewId2 = reviewService.saveReview(memberId, placeId, contents, 4.0);

        //then
        Place place1 = placeService.findOne(placeId);
        assertEquals(2, place1.getReviewCount());
        assertEquals(4.5, place1.getAvgStar());
    }

    @Test
    public void 리뷰삭제_부수효과() throws Exception {
        //given
        Member member = new Member();
        member.setEmail("회원1");
        Long memberId = memberService.join(member);

        Place place = new Place();
        place.setName("식당1");
        Coordinate coordinate = new Coordinate(123.1, 321.3);
        place.setCoordinate(coordinate);
        Long placeId = placeService.savePlace(place);

        //when
        String contents = "맛있다!";
        Long reviewId1 = reviewService.saveReview(memberId, placeId, contents, 5.0);
        Long reviewId2 = reviewService.saveReview(memberId, placeId, contents, 4.0);
        reviewService.deleteReview(reviewId2);

        //then
        Review review1 = reviewService.findOne(reviewId2);
        Place place1 = placeService.findOne(placeId);
        assertEquals(ReviewStatus.DELETED, review1.getReviewStatus());
        assertEquals(1, place1.getReviewCount());
        assertEquals(5, place1.getAvgStar());

        assertEquals(ReviewStatus.DELETED, member.getReviews().get(1).getReviewStatus());
    }
}
