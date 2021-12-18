package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Meet;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.MemberMeet;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.exception.HeadMemberException;
import com.bobmate.bobmate.repository.MeetRepository;
import com.bobmate.bobmate.repository.MemberMeetRepository;
import com.bobmate.bobmate.repository.MemberRepository;
import com.bobmate.bobmate.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetService {

    private final MemberRepository memberRepository;
    private final MeetRepository meetRepository;
    private final PlaceRepository placeRepository;
    private final MemberMeetRepository memberMeetRepository;

    @Transactional
    public Long saveMeet(Long memberId, Long placeId, String name, String link) {
        Member headMember = memberRepository.findOne(memberId);
        Place place = placeRepository.findOne(placeId);

        MemberMeet memberMeet = MemberMeet.createMemberMeet(headMember);

        Meet meet = Meet.createMeet(headMember, memberMeet, place, name, link);

        meetRepository.save(meet);
        memberMeetRepository.save(memberMeet);
        return meet.getId();
    }

    @Transactional
    public Long addMember(Long memberId, Long meetId) {
        Member member = memberRepository.findOne(memberId);
        Meet meet = meetRepository.findOne(meetId);

        MemberMeet memberMeet = MemberMeet.createMemberMeet(member);
        meet.addMember(memberMeet);
        memberMeetRepository.save(memberMeet);
        return memberMeet.getId();
    }

    @Transactional
    public void deleteMember(Long memberId, Long meetId) {
        Member member = memberRepository.findOne(memberId);
        Meet meet = meetRepository.findOne(meetId);

        MemberMeet memberMeet = memberMeetRepository.findOneByMemberIdAndMeetId(member, meet);
        if (member == meet.getHeadMember()) {
            throw new HeadMemberException("모임 방장은 모임을 삭제하거나 다른 사람에게 방장을 넘겨주기 전까지는 탈퇴할 수 없습니다.");
        }
        memberMeetRepository.delete(memberMeet);
    }

    public Meet findOne(Long meetId) {
        return meetRepository.findOne(meetId);
    }
}
