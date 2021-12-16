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

    public void save(Meet meet) {
        em.persist(meet);
    }

    public Meet findOne(Long id) {
        return em.find(Meet.class, id);
    }

    public List<Meet> findAll() {
        return em.createQuery("select meet from Meet meet", Meet.class)
                .getResultList();
    }
}
