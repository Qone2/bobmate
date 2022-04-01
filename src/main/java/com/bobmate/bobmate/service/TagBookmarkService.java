package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Tag;
import com.bobmate.bobmate.domain.TagBookmark;
import com.bobmate.bobmate.exception.TagBookmarkDuplicateException;
import com.bobmate.bobmate.exception.TagBookmarkMemberException;
import com.bobmate.bobmate.repository.BookmarkRepository;
import com.bobmate.bobmate.repository.MemberRepository;
import com.bobmate.bobmate.repository.TagBookmarkRepository;
import com.bobmate.bobmate.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagBookmarkService {

    private final TagBookmarkRepository tagBookmarkRepository;
    private final TagRepository tagRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;

    /**
     * 태그북마크 생성
     */
    @Transactional
    public Long saveTagBookmark(Long tagId, Long bookmarkId, Long memberId) {
        Tag tag = tagRepository.findOne(tagId);
        Bookmark bookmark = bookmarkRepository.findOne(bookmarkId);
        Member member = memberRepository.findOne(memberId);

        TagBookmark tagBookmark = TagBookmark.createTagBookmark(tag, bookmark, member);

        validateDuplicateTagBookmark(tag, bookmark, member);
        validateBookmarkMember(bookmark, member);
        tagBookmarkRepository.save(tagBookmark);
        return tagBookmark.getId();
    }

    /**
     * 태그북마크 중복 확인
     */
    public void validateDuplicateTagBookmark(Tag tag, Bookmark bookmark, Member member) {
        TagBookmark findTagBookmark = tagBookmarkRepository.findOneByTagIdAndBookmarkIdAndMemberId(tag, bookmark, member);
        if (findTagBookmark != null) {
            throw new TagBookmarkDuplicateException("이미 생성되어있는 북마크 입니다.");
        }
    }

    /**
     * 태그북마크 멤버 확인
     */
    public void validateBookmarkMember(Bookmark bookmark, Member member) {
        if (bookmark.getMember() != member) {
            throw new TagBookmarkMemberException("북마크의 멤버와 신청하는 멤버가 일치하지 않습니다.");
        }
    }

    /**
     * 태그북마크 삭제
     */
    @Transactional
    public void deleteTagBookmark(Long tagId, Long bookmarkId, Long memberId) {
        Tag tag = tagRepository.findOne(tagId);
        Bookmark bookmark = bookmarkRepository.findOne(bookmarkId);
        Member member = memberRepository.findOne(memberId);

        TagBookmark tagBookmark = tagBookmarkRepository.findOneByTagIdAndBookmarkIdAndMemberId(tag, bookmark, member);

        tagBookmarkRepository.delete(tagBookmark);
    }

    /**
     * 태그북마크 검색
     * tagIdList를 참고하여 AND 연산으로 포함되는 태그들을 검색
     */
    public List<Bookmark> findTaggedBookmark(Long memberId, List<Long> tagIdList) {
        Member member = memberRepository.findOne(memberId);
        Set<Bookmark> bookmarkSet = new HashSet<>();
        boolean isFirst = true;

        for (Long tagId : tagIdList) {
            Tag tag = tagRepository.findOne(tagId);
            if (isFirst) {
                bookmarkSet.addAll(tagBookmarkRepository.findAllByTagIdAndMemberId(tag, member)
                        .stream().map(tb -> tb.getBookmark()).collect(Collectors.toList()));
                isFirst = false;
            } else {
                bookmarkSet.retainAll(tagBookmarkRepository.findAllByTagIdAndMemberId(tag, member)
                        .stream().map(tb -> tb.getBookmark()).collect(Collectors.toList()));
            }
        }

        return new ArrayList<>(bookmarkSet);
    }
}
