package com.bobmate.bobmate.repository;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Tag;
import com.bobmate.bobmate.domain.TagBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagBookmarkRepository {

    private final EntityManager em;

    /**
     * 태그북마크 저장(북마크에 태그걸기)
     */
    public void save(TagBookmark tagBookmark) {
        em.persist(tagBookmark);
    }

    /**
     * 태그북마크 조회
     */
    public TagBookmark findOne(Long id) {
        return em.find(TagBookmark.class, id);
    }

    /**
     * 태그북마크 전체조회
     */
    public List<TagBookmark> findAll() {
        return em.createQuery("select t from TagBookmark t", TagBookmark.class)
                .getResultList();
    }

    /**
     * 태그북마크 삭제(북마크에 태그 해제)
     */
    public void delete(TagBookmark tagBookmark) {
        em.remove(tagBookmark);
        em.flush();
        em.clear();
    }

    /**
     * 태그id와 북마크id로 조회
     */
    public TagBookmark findOneByTagIdAndBookmarkId(Tag tag, Bookmark bookmark) {
        return em.createQuery("select tb from TagBookmark tb " +
                "where tb.tag = :tag and tb.bookmark = :bookmark", TagBookmark.class)
                .setParameter("tag", tag)
                .setParameter("bookmark", bookmark)
                .getResultList().stream().findFirst().orElse(null);
    }

    /**
     * 태그id와 멤버id로 전체 조회
     */
    public List<TagBookmark> findAllByTagIdAndMemberId(Tag tag, Member member) {
        return em.createQuery("select tb from TagBookmark tb " +
                "where tb.bookmark.member = :member and tb.tag = :tag", TagBookmark.class)
                .setParameter("tag", tag)
                .setParameter("member", member)
                .getResultList();
    }

    /**
     * 특정 맴버아이디를 가진 모든 태그북마크 조회
     */
    public List<TagBookmark> findAllByMemberId(Member member) {
        return em.createQuery("select tb from TagBookmark tb " +
                        "where tb.bookmark.member = :member", TagBookmark.class)
                .setParameter("member", member)
                .getResultList();
    }
}
