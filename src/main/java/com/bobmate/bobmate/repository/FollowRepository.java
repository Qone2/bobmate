package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Follow;
import com.bobmate.bobmate.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

    private final EntityManager em;

    /**
     * 팔로우 저장(팔로우)
     */
    public void save(Follow follow) {
        em.persist(follow);
    }

    /**
     * 팔로우 단일 조회
     */
    public Follow findOne(Long id) {
        return em.find(Follow.class, id);
    }

    /**
     * 팔로우 전체 조회
     */
    public List<Follow> findAll() {
        return em.createQuery("select f from Follow f", Follow.class)
                .getResultList();
    }

    /**
     * 팔로우 팔로워id와 팔로이id로 조회
     */
    public Follow findOneByFromMemberIdAndToMemberId(Member fromMember, Member toMember) {
        return em.createQuery("select f from Follow f " +
                "where f.fromMember = :fromMember and f.toMember = :toMember", Follow.class)
                .setParameter("fromMember", fromMember)
                .setParameter("toMember", toMember)
                .getResultList().stream().findFirst().orElse(null);
    }

    /**
     * 팔로우 삭제(팔로우 취소)
     */
    public void delete(Follow follow) {
        em.remove(follow);
        em.flush();
        em.clear();
    }
}
