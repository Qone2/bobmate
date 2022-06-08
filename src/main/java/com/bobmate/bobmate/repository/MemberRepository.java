package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    /**
     * 멤버 저장
     */
    public void save(Member member) {
        em.persist(member);
    }

    /**
     * 멤버 단일 조회
     */
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    /**
     * 멤버 전체 조회
     */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * 이메일로 멤버 조회
     */
    public Optional<Member> findOneByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst();
    }

    /**
     * 멤버 삭제
     */
    public void delete(Member member) {
        em.remove(member);
        em.flush();
        em.clear();
    }

    /**
     * fetch join을 사용한 전체 조회
     */
    public List<Member> findAllFetch() {
        return em.createQuery("select m from Member m " +
                "join fetch m.reviews " +
                "join fetch m.memberMeets mm " +
                "join fetch mm.meet " +
                "join fetch m.likeReviews lr " +
                "join fetch lr.review " +
                "join fetch m.followers fer " +
                "join fetch fer.fromMember " +
                "join fetch m.following fing " +
                "join fetch fing.toMember " +
                "join fetch m.bookmarks", Member.class)
                .getResultList();
    }

    /**
     * fetch join을 사용한 단일 조회
     */
    public Member findOneFetch(Long id) {
        return em.createQuery("select m from Member m " +
                        "join fetch m.reviews " +
                        "join fetch m.memberMeets mm " +
                        "join fetch mm.meet " +
                        "join fetch m.likeReviews lr " +
                        "join fetch lr.review " +
                        "join fetch m.followers fer " +
                        "join fetch fer.fromMember " +
                        "join fetch m.following fing " +
                        "join fetch fing.toMember " +
                        "join fetch m.bookmarks " +
                        "where m.id = :id", Member.class)
                .setParameter("id", id)
                .getResultList().stream().findFirst().orElse(null);
    }
}
