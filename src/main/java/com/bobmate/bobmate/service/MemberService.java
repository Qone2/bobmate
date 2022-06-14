package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.MemberStatus;
import com.bobmate.bobmate.dto.CreateMemberDto;
import com.bobmate.bobmate.exception.DeletedMemberException;
import com.bobmate.bobmate.exception.UserNameDuplicateException;
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
    public Long join(CreateMemberDto memberDto) {
        validDuplicateMember(memberDto);
        Member member = Member.createMember(memberDto);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원가입 중복조회
     * 병렬작업으로 인해 제대로 동작하지 않을 가능성이 있어
     * entity에 unique constraint 필요
     */
    private void validDuplicateMember(CreateMemberDto memberDto) {
        Optional<Member> findMember = memberRepository.findOneByUserName(memberDto.getUserName());
        if (findMember.isPresent()) {
            throw new UserNameDuplicateException("이미 존재하는 아이디 입니다.");
        }
    }

    /**
     * 멤버 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void update(Long memberId, String userName) {
        Member findMember = memberRepository.findOne(memberId);
        findMember.setUserName(userName);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findByUserName(String userName) {
        return memberRepository.findOneByUserName(userName);
    }

    /**
     * 멤버 삭제(논리 삭제)
     */
    @Transactional
    public Long deleteMember(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        if (member.getMemberStatus() == MemberStatus.DELETED) {
            throw new DeletedMemberException("이미 삭제된 멤버 입니다.");
        }
        member.delete();
        return member.getId();
    }

}
