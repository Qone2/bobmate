package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.domain.Review;
import com.bobmate.bobmate.repository.MemberRepository;
import com.bobmate.bobmate.repository.PlaceRepository;
import com.bobmate.bobmate.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    /**
     * 리뷰생성
     */
    @Transactional
    public Long saveReview(Long memberId, Long placeId, String contents, Double star) {
        Member member = memberRepository.findOne(memberId);
        Place place = placeRepository.findOne(placeId);

        Review review = Review.createReview(member, place, contents, star);
        reviewRepository.save(review);
        return review.getId();
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findOne(reviewId);
        review.delete();
    }

    public Review findOne(Long reviewId) {
        return reviewRepository.findOne(reviewId);
    }
}
