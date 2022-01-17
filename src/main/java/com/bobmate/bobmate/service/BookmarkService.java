package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.repository.BookmarkRepository;
import com.bobmate.bobmate.repository.MemberRepository;
import com.bobmate.bobmate.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Bookmark bookmark = Bookmark.createBookmark(member, place);

        bookmarkRepository.save(bookmark);
        return bookmark.getId();
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
}
