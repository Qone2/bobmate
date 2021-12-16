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

    public void save(MemberMeet memberMeet) {
        em.persist(memberMeet);
    }

    public MemberMeet findOne(Long id) {
        return em.find(MemberMeet.class, id);
    }

    public List<MemberMeet> findAll() {
        return em.createQuery("select m from MemberMeet m", MemberMeet.class)
                .getResultList();
    }

    public MemberMeet findOneByMemberIdAndMeetId(Member member, Meet meet) {
        return em.createQuery("select m from MemberMeet m " +
                "where m.member = :member and m.meet = :meet", MemberMeet.class)
                .setParameter("member", member)
                .setParameter("meet", meet)
                .getSingleResult();
    }

    public void delete(MemberMeet memberMeet) {
        em.remove(memberMeet);
    }
}
