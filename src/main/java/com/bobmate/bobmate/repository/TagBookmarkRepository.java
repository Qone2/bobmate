package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.TagBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagBookmarkRepository {

    private final EntityManager em;

    public void save(TagBookmark tagBookmark) {
        em.persist(tagBookmark);
    }

    public TagBookmark findOne(Long id) {
        return em.find(TagBookmark.class, id);
    }

    public List<TagBookmark> findAll() {
        return em.createQuery("select t from TagBookmark t", TagBookmark.class)
                .getResultList();
    }

    public void delete(TagBookmark tagBookmark) {
        em.remove(tagBookmark);
        em.flush();
        em.clear();
    }
}
