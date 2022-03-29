package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final EntityManager em;

    /**
     * 태그 생성
     */
    public void save(Tag tag) {
        em.persist(tag);
    }

    /**
     * 태그 단일 조회
     */
    public Tag findOne(Long id) {
        return em.find(Tag.class, id);
    }

    /**
     * 이름으로 태그 단일 조회
     */
    public Tag findOneByName(String name) {
        return em.createQuery("select t from Tag t " +
                "where t.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultList().stream().findFirst().orElse(null);
    }

    /**
     * 태그 전체 조회
     */
    public List<Tag> findAll() {
        return em.createQuery("select t from Tag t", Tag.class)
                .getResultList();
    }

    /**
     * 태그 삭제
     */
    public void delete(Tag tag) {
        em.remove(tag);
        em.flush();
        em.clear();
    }
}
