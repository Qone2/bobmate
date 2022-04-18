package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Meet;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.MemberMeet;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.exception.HeadMemberException;
import com.bobmate.bobmate.exception.MemberMeetDuplicateException;
import com.bobmate.bobmate.repository.MeetRepository;
import com.bobmate.bobmate.repository.MemberMeetRepository;
import com.bobmate.bobmate.repository.MemberRepository;
import com.bobmate.bobmate.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetService {

    private final MemberRepository memberRepository;
    private final MeetRepository meetRepository;
    private final PlaceRepository placeRepository;
    private final MemberMeetRepository memberMeetRepository;

    /**
     * 모임 등록
     */
    @Transactional
    public Long saveMeet(Long memberId, Long placeId, String name, String link) {
        Member headMember = memberRepository.findOne(memberId);
        Place place = placeRepository.findOne(placeId);

        Meet meet = Meet.createMeet(headMember, place, name, link);

        MemberMeet memberMeet = MemberMeet.createMemberMeet(headMember, meet);

        meetRepository.save(meet);
        memberMeetRepository.save(memberMeet);
        return meet.getId();
    }

    /**
     * 모임에 맴버 추가
     */
    @Transactional
    public Long addMember(Long memberId, Long meetId) {
        Member member = memberRepository.findOne(memberId);
        Meet meet = meetRepository.findOne(meetId);

        MemberMeet memberMeet = MemberMeet.createMemberMeet(member, meet);
        validateDuplicateMemberMeet(member, meet);
        memberMeetRepository.save(memberMeet);
        return memberMeet.getId();
    }

    /**
     * 멤버가 모임에 중복 참여하는지 확인
     */
    public void validateDuplicateMemberMeet(Member member, Meet meet) {
        MemberMeet findMemberMeet = memberMeetRepository.findOneByMemberIdAndMeetId(member, meet);
        if (findMemberMeet != null) {
            throw new MemberMeetDuplicateException("이미 모임에 참여한 멤버입니다.");
        }
    }

    /**
     * 모임에 멤버 삭제
     */
    @Transactional
    public Long deleteMember(Long memberId, Long meetId) {
        Member member = memberRepository.findOne(memberId);
        Meet meet = meetRepository.findOne(meetId);

        MemberMeet memberMeet = memberMeetRepository.findOneByMemberIdAndMeetId(member, meet);
        if (member == meet.getHeadMember()) {
            throw new HeadMemberException("모임 방장은 모임을 삭제하거나 다른 사람에게 방장을 넘겨주기 전까지는 탈퇴할 수 없습니다.");
        }
        memberMeetRepository.delete(memberMeet);
        meet = meetRepository.findOne(meetId);
        meet.setMemberCount();
        return memberMeet.getId();
    }

    public Meet findOne(Long meetId) {
        return meetRepository.findOne(meetId);
    }

    public List<Meet> findAll() {
        return meetRepository.findAll();
    }
}
