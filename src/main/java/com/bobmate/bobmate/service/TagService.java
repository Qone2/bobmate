package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.Tag;
import com.bobmate.bobmate.domain.TagBookmark;
import com.bobmate.bobmate.repository.TagBookmarkRepository;
import com.bobmate.bobmate.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;
    private final TagBookmarkRepository tagBookmarkRepository;
    private final MemberService memberService;


    /**
     * 태그 생성
     */
    @Transactional
    public Long saveTag(String name) {
        Tag tag = Tag.createTag(name);
        tagRepository.save(tag);
        return tag.getId();
    }


    /**
     * 태그 전체 조회
     */
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }


    /**
     * 태그 삭제
     */
    @Transactional
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findOne(tagId);
        tagRepository.delete(tag);
    }

    /**
     * 태그 단일 조회
     */
    public Tag findOne(Long id) {
        return tagRepository.findOne(id);
    }

    /**
     * 태그된 개수별 내림차순 조회
     */
    public List<Map.Entry<Long, Integer>> findAllByTaggedCount() {
        List<TagBookmark> tagBookmarkList = tagBookmarkRepository.findAll();
        HashMap<Long, Integer> map = new HashMap<>();
        for (TagBookmark tagBookmark : tagBookmarkList) {
            Long id = tagBookmark.getTag().getId();
            if (map.containsKey(id)) {
                map.put(id, 1);
            } else {
                map.put(id, map.get(id) + 1);
            }
        }

        List<Map.Entry<Long, Integer>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort(new Comparator<Map.Entry<Long, Integer>>() {
            @Override
            public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });

        return entryList;
    }

    /**
     * 맴버별 태그된 개수별 내림차순 조회
     */
    public List<Map<Long, Integer>> findAllByMemberAndTaggedCount(Long memberId) {
        Member findMember = memberService.findOne(memberId);
        List<TagBookmark> tagBookmarkList = tagBookmarkRepository.findAllByMemberId(findMember);
        HashMap<Long, Integer> map = new HashMap<>();
        for (TagBookmark tagBookmark : tagBookmarkList) {
            Long id = tagBookmark.getTag().getId();
            if (map.containsKey(id)) {
                map.put(id, 1);
            } else {
                map.put(id, map.get(id) + 1);
            }
        }

        List<Map.Entry<Long, Integer>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort(new Comparator<Map.Entry<Long, Integer>>() {
            @Override
            public int compare(Map.Entry<Long, Integer> o1, Map.Entry<Long, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });

        return entryList;
    }
}
