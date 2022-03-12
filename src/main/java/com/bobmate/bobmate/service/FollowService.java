package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Follow;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.repository.FollowRepository;
import com.bobmate.bobmate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    /**
     * 팔로우
     */
    @Transactional
    public Long follow(Long fromId, Long toId) {
        Member fromMember = memberRepository.findOne(fromId);
        Member toMember = memberRepository.findOne(toId);

        Follow follow = Follow.createFollow(fromMember, toMember);

        followRepository.save(follow);
        return follow.getId();
    }

    /**
     * 언팔로우
     */
    @Transactional
    public void unfollow(Long fromId, Long toId) {
        Member fromMember = memberRepository.findOne(fromId);
        Member toMember = memberRepository.findOne(toId);

        Follow follow = followRepository.findOneByFromMemberIdAndToMemberId(fromMember, toMember);
        followRepository.delete(follow);
    }

    /**
     * 팔로우 전체 조회
     */
    public List<Follow> findAll() {
        return followRepository.findAll();
    }
}
