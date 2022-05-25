package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.*;
import com.bobmate.bobmate.exception.DeletedPlaceException;
import com.bobmate.bobmate.repository.MemberRepository;
import com.bobmate.bobmate.repository.PhotoRepository;
import com.bobmate.bobmate.repository.PlaceRepository;
import com.bobmate.bobmate.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final PhotoRepository photoRepository;

    /**
     * 리뷰생성
     */
    @Transactional
    public Long saveReview(Long memberId, Long placeId, String contents, Double star, List<Photo> photoList) {
        Member member = memberRepository.findOne(memberId);
        Place place = placeRepository.findOne(placeId);
        if (place.getPlaceStatus() == PlaceStatus.DELETED) {
            throw new DeletedPlaceException("해당 장소는 삭제된 장소 입니다.");
        }

        Review review = Review.createReview(member, place, contents, star);
        reviewRepository.save(review);
        for (Photo p : photoList) {
            Photo photo = Photo.createPhoto(review, p.getOriginalFileName(), p.getFilePath(), p.getFileSize());
            photoRepository.save(photo);
        }
        return review.getId();
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public Long deleteReview(Long reviewId) {
        Review review = reviewRepository.findOne(reviewId);
        review.delete();
        return review.getId();
    }

    public Review findOne(Long reviewId) {
        return reviewRepository.findOne(reviewId);
    }

    @Transactional
    public Long updateReview(Long reviewId, String contents, Double star) {
        Review review = reviewRepository.findOne(reviewId);
        review.setContent(contents);
        review.setStar(star);

        return review.getId();
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
}
