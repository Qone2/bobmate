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
    public Optional<Member> findOneByUserName(String userName) {
        return em.createQuery("select m from Member m where m.userName = :userName", Member.class)
                .setParameter("userName", userName)
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
