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
     * 아이디로 멤버 조회
     */
    public Optional<Member> findOneByUserId(String userId) {
        return em.createQuery("select m from Member m where m.userId = :userId", Member.class)
                .setParameter("userId", userId)
                .getResultList().stream().findFirst();
    }

    /**
     * 닉네임으로 멤버 조회
     */
    public Optional<Member> findOneByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
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
}
