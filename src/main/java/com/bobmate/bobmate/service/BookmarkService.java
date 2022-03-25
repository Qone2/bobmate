package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.exception.BookmarkDuplicateException;
import com.bobmate.bobmate.repository.BookmarkRepository;
import com.bobmate.bobmate.repository.MemberRepository;
import com.bobmate.bobmate.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    /**
     * 북마크 설정
     */
    @Transactional
    public Long saveBookmark(Long memberId, Long placeId) {
        Member member = memberRepository.findOne(memberId);
        Place place = placeRepository.findOne(placeId);

        validateDuplicateBookmark(member, place);

        Bookmark bookmark = Bookmark.createBookmark(member, place);

        bookmarkRepository.save(bookmark);
        return bookmark.getId();
    }

    /**
     * 북마크 중복 확인
     */
    public void validateDuplicateBookmark(Member member, Place place) {
        Bookmark findBookmark = bookmarkRepository.findOneByMemberIdAndPlaceId(member, place);
        if (findBookmark != null) {
            throw new BookmarkDuplicateException("이미 생성된 북마크 입니다.");
        }
    }

    /**
     * 북마크 삭제
     */
    @Transactional
    public void deleteBookmark(Long memberId, Long placeId) {
        Member member = memberRepository.findOne(memberId);
        Place place = placeRepository.findOne(placeId);

        Bookmark bookmark = bookmarkRepository.findOneByMemberIdAndPlaceId(member, place);

        bookmarkRepository.delete(bookmark);
    }

    /**
     * 북마크 단일 조회
     */
    public Bookmark findOne(Long bookmarkId) {
        return bookmarkRepository.findOne(bookmarkId);
    }

    /**
     * 북마크 전체 조회
     */
    public List<Bookmark> findAll() {
        return bookmarkRepository.findAll();
    }

}
