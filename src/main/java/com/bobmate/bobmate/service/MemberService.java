package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원가입 중복조회
     * 병렬작업으로 인해 제대로 동작하지 않을 가능성이 있어
     * entity에 unique constraint 필요
     */
    private void validDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원명 입니다.");
        }
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void update(Long memberId, String email) {
        Member findMember = memberRepository.findOne(memberId);
        findMember.setEmail(email);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void deleteMember(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        memberRepository.delete(member);
    }
}
