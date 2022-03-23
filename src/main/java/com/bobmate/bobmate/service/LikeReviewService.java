package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.LikeReview;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Review;
import com.bobmate.bobmate.exception.LikeDuplicateException;
import com.bobmate.bobmate.repository.LikeReviewRepository;
import com.bobmate.bobmate.repository.MemberRepository;
import com.bobmate.bobmate.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeReviewService {

    private final LikeReviewRepository likeReviewRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰에 좋아요
     */
    @Transactional
    public Long likeReview(Long memberId, Long reviewId) {
        Member member = memberRepository.findOne(memberId);
        Review review = reviewRepository.findOne(reviewId);

        validDuplicateLike(member, review);
        LikeReview likeReview = LikeReview.createLikeReview(member, review);
        likeReviewRepository.save(likeReview);

        return likeReview.getId();
    }

    /**
     * 중복 좋아요 확인
     */
    public void validDuplicateLike(Member member, Review review) {
        LikeReview findLikeReview = likeReviewRepository.findOneByMemberIdAndReviewId(member, review);
        if (findLikeReview != null) {
            throw new LikeDuplicateException("좋아요를 두번 할 수는 없습니다.");
        }
    }

    /**
     * 리뷰에 좋아요 취소
     */
    @Transactional
    public void unlikeReview(Long memberId, Long reviewId) {
        Member member = memberRepository.findOne(memberId);
        Review review = reviewRepository.findOne(reviewId);

        LikeReview likeReview = likeReviewRepository.findOneByMemberIdAndReviewId(member, review);
        likeReviewRepository.delete(likeReview);
    }

    /**
     * 리뷰좋아요 전체 조회
     */
    public List<LikeReview> findAll() {
        return likeReviewRepository.findAll();
    }

    /**
     * 리뷰좋아요 단일 조회
     */
    public LikeReview findOne(Long id) {
        return likeReviewRepository.findOne(id);
    }
}
