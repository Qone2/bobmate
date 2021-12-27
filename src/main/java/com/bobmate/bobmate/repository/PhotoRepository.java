package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {

    private final EntityManager em;

    public void save(Photo photo) {
        em.persist(photo);
    }

    public Photo findOne(Long photoId) {
        return em.find(Photo.class, photoId);
    }

    public List<Photo> findAll() {
        return em.createQuery("select p from Photo p", Photo.class)
                .getResultList();
    }
}
