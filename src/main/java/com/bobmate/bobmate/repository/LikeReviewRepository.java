package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.LikeReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeReviewRepository {

    private final EntityManager em;

    public void save(LikeReview likeReview) {
        em.persist(likeReview);
    }

    public LikeReview findOne(Long id) {
        return em.find(LikeReview.class, id);
    }

    public List<LikeReview> findAll() {
        return em.createQuery("select l from LikeReview l", LikeReview.class)
                .getResultList();
    }
}
