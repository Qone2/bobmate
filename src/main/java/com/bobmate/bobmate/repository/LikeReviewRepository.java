package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.LikeReview;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeReviewRepository {

    private final EntityManager em;

    /**
     * 리뷰좋아요 저장(리뷰에 좋아요)
     */
    public void save(LikeReview likeReview) {
        em.persist(likeReview);
    }

    /**
     * 리뷰좋아요 단일 조회
     */
    public LikeReview findOne(Long id) {
        return em.find(LikeReview.class, id);
    }

    /**
     * 리뷰좋아요 전체 조회
     */
    public List<LikeReview> findAll() {
        return em.createQuery("select l from LikeReview l", LikeReview.class)
                .getResultList();
    }

    /**
     * 리뷰좋아요 멤버id와 리뷰id로 조회
     */
    public LikeReview findOneByMemberIdAndReviewId(Member member, Review review) {
        return em.createQuery("select l from LikeReview l where l.member = :member and l.review = :review",
                LikeReview.class).setParameter("member", member).setParameter("review", review)
                .getResultList().stream().findFirst().orElse(null);
    }

    /**
     * 리뷰좋아요 삭제(리뷰에 좋아요 취소)
     */
    public void delete(LikeReview likeReview) {
        em.remove(likeReview);
        em.flush();
        em.clear();
    }
}
