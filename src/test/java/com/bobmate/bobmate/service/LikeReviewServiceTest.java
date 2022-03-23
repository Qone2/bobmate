package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.domain.Review;
import com.bobmate.bobmate.exception.LikeDuplicateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikeReviewServiceTest {

    @Autowired
    LikeReviewService likeReviewService;
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PlaceService placeService;
    @Autowired
    ReviewService reviewService;

    @Test
    public void 중복좋아요() throws Exception {
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
        Long reviewId = reviewService.saveReview(memberId, placeId, contents, 5.0, new ArrayList<>());
        Review review = reviewService.findOne(reviewId);
        likeReviewService.likeReview(memberId, reviewId);

        //then
        assertThrows(LikeDuplicateException.class, () -> likeReviewService.likeReview(memberId, reviewId));
    }

}
