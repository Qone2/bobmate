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

    /**
     * 사진 저장
     */
    public void save(Photo photo) {
        em.persist(photo);
    }

    /**
     * 사진 단일 조회
     */
    public Photo findOne(Long photoId) {
        return em.find(Photo.class, photoId);
    }

    /**
     * 사진 전체 조회
     */
    public List<Photo> findAll() {
        return em.createQuery("select p from Photo p", Photo.class)
                .getResultList();
    }

    /**
     * 사진 삭제
     */
    public void delete(Photo photo) {
        em.remove(photo);
        em.flush();
        em.clear();
    }
}
