package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Meet;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.MemberMeet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberMeetRepository {

    private final EntityManager em;

    /**
     * 멤버모임 저장(모임에 멤버 참가)
     */
    public void save(MemberMeet memberMeet) {
        em.persist(memberMeet);
    }

    /**
     * 멤버모임 단일 조회
     */
    public MemberMeet findOne(Long id) {
        return em.find(MemberMeet.class, id);
    }

    /**
     * 멤버모임 전체 조회
     */
    public List<MemberMeet> findAll() {
        return em.createQuery("select m from MemberMeet m", MemberMeet.class)
                .getResultList();
    }

    /**
     * 멤버모임 멤버id와 모임id로 조회
     */
    public MemberMeet findOneByMemberIdAndMeetId(Member member, Meet meet) {
        return em.createQuery("select m from MemberMeet m " +
                "where m.member = :member and m.meet = :meet", MemberMeet.class)
                .setParameter("member", member)
                .setParameter("meet", meet)
                .getResultList().stream().findFirst().orElse(null);
    }

    /**
     * 멤버모임 삭제(모임에 멤버 탈퇴)
     */
    public void delete(MemberMeet memberMeet) {
        em.remove(memberMeet);
        em.flush();
        em.clear();
    }
}
