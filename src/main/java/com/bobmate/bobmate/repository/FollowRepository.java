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

    public void save(Follow follow) {
        em.persist(follow);
    }

    public Follow findOne(Long id) {
        return em.find(Follow.class, id);
    }

    public List<Follow> findAll() {
        return em.createQuery("select f from Follow f", Follow.class)
                .getResultList();
    }

    public Follow findOneByFromMemberIdAndToMemberId(Member fromMember, Member toMember) {
        return em.createQuery("select f from Follow f " +
                "where f.fromMember = :fromMember and f.toMember = :toMember", Follow.class)
                .setParameter("fromMember", fromMember)
                .setParameter("toMember", toMember)
                .getResultList().stream().findFirst().orElse(null);
    }

    public void delete(Follow follow) {
        em.remove(follow);
        em.flush();
        em.clear();
    }
}
