package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Tag;
import com.bobmate.bobmate.domain.TagBookmark;
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

    @Transactional
    public Long saveTagBookmark(Long tagId, Long bookmarkId, Long memberId) {
        Tag tag = tagRepository.findOne(tagId);
        Bookmark bookmark = bookmarkRepository.findOne(bookmarkId);
        Member member = memberRepository.findOne(memberId);

        TagBookmark tagBookmark = TagBookmark.createTagBookmark(tag, bookmark, member);

        tagBookmarkRepository.save(tagBookmark);
        return tagBookmark.getId();
    }

    @Transactional
    public void deleteTagBookmark(Long tagId, Long bookmarkId, Long memberId) {
        Tag tag = tagRepository.findOne(tagId);
        Bookmark bookmark = bookmarkRepository.findOne(bookmarkId);
        Member member = memberRepository.findOne(memberId);

        TagBookmark tagBookmark = tagBookmarkRepository.findOneByTagIdAndBookmarkIdAndMemberId(tag, bookmark, member);

        tagBookmarkRepository.delete(tagBookmark);
    }

    public List<Bookmark> findTaggedBookmark(Long memberId, List<Long> tagIdList) {
        Member member = memberRepository.findOne(memberId);
        Set<Bookmark> bookmarkSet = new HashSet<>();

        for (Long tagId : tagIdList) {
            Tag tag = tagRepository.findOne(tagId);
            if (bookmarkSet.isEmpty()) {
                bookmarkSet.addAll(tagBookmarkRepository.findAllByTagIdAndMemberId(tag, member)
                        .stream().map(tb -> tb.getBookmark()).collect(Collectors.toList()));
            } else {
                bookmarkSet.retainAll(tagBookmarkRepository.findAllByTagIdAndMemberId(tag, member)
                        .stream().map(tb -> tb.getBookmark()).collect(Collectors.toList()));
            }
        }

        return new ArrayList<>(bookmarkSet);
    }
}
