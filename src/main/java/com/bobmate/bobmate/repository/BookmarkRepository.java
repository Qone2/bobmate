package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkRepository {

    private final EntityManager em;

    public void save(Bookmark bookmark) {
        em.persist(bookmark);
    }

    public Bookmark findOne(Long id) {
        return em.find(Bookmark.class, id);
    }

    public List<Bookmark> findAll() {
        return em.createQuery("select b from Bookmark b", Bookmark.class)
                .getResultList();
    }

    public Bookmark findOneByMemberIdAndPlaceId(Member member, Place place) {
        return em.createQuery("select b from Bookmark b " +
                "where b.member = :member and b.place = :place", Bookmark.class)
                .setParameter("member", member)
                .setParameter("place", place)
                .getResultList().stream().findFirst().orElse(null);
    }

    public void delete(Bookmark bookmark) {
        em.remove(bookmark);
        em.flush();
        em.clear();
    }
}
