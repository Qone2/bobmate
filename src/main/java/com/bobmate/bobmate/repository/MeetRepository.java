package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Meet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeetRepository {

    private final EntityManager em;

    /**
     * 모임 저장
     */
    public void save(Meet meet) {
        em.persist(meet);
    }

    /**
     * 모임 단일 조회
     */
    public Meet findOne(Long id) {
        return em.find(Meet.class, id);
    }

    /**
     * 모임 전체 조회
     */
    public List<Meet> findAll() {
        return em.createQuery("select meet from Meet meet", Meet.class)
                .getResultList();
    }

    /**
     * 모임 삭제
     */
    public void delete(Meet meet) {
        em.remove(meet);
        em.flush();
        em.clear();
    }
}
