package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MeetServiceTest {

    @Autowired MemberService memberService;
    @Autowired PlaceService placeService;
    @Autowired MeetService meetService;

    @Test
    public void 모임생성() throws Exception {
        //given
        Member member = new Member();
        member.setName("멤버1");
        memberService.join(member);

        Place place = new Place();
        place.setName("식당1");
        place.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place);

        //when
        Long meetId = meetService.saveMeet(member.getId(), place.getId(), "모임1", "http://dfsf.c");
        Meet meet = meetService.findOne(meetId);

        //then
        assertEquals(meet.getHeadMember(), member);
        assertEquals(meet.getPlace(), place);
        assertEquals(meet.getMemberMeets().size(), 1);

        assertEquals(member.getMemberMeets().size(), 1);

        assertEquals(member.getMemberMeets().get(0), meet.getMemberMeets().get(0));

        assertEquals(place.getMeets().size(), 1);
        assertEquals(place.getMeets().get(0), meet);
    }

    @Test
    public void 멤버추가() throws Exception {
        //given
        Member member = new Member();
        member.setName("멤버0");
        memberService.join(member);
        Member member1 = new Member();
        member1.setName("멤버1");
        memberService.join(member1);
        Member member2 = new Member();
        member2.setName("멤버2");
        memberService.join(member2);

        Place place = new Place();
        place.setName("식당0");
        place.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place);

        //when
        Long meetId = meetService.saveMeet(member.getId(), place.getId(), "모임0", "http://dfsf.c");
        meetService.addMember(member1.getId(), meetId);
        meetService.addMember(member2.getId(), meetId);
        Meet meet = meetService.findOne(meetId);

        //then
        assertEquals(meet.getMemberMeets().size(), 3);
        assertEquals(member.getMemberMeets().get(0).getMeet(), member1.getMemberMeets().get(0).getMeet());
        assertEquals(member.getMemberMeets().get(0).getMeet(), member2.getMemberMeets().get(0).getMeet());

        assertEquals(place.getMeets().size(), 1);
        assertEquals(place.getMeets().get(0), meet);
    }

    @Test
    public void 멤버삭제() throws Exception {
        //given
        Member member = new Member();
        member.setName("멤버0");
        memberService.join(member);
        Member member1 = new Member();
        member1.setName("멤버1");
        memberService.join(member1);
        Member member2 = new Member();
        member2.setName("멤버2");
        memberService.join(member2);

        Place place = new Place();
        place.setName("식당0");
        place.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place);

        //when
        Long meetId = meetService.saveMeet(member.getId(), place.getId(), "모임0", "http://dfsf.c");
        meetService.addMember(member1.getId(), meetId);
        meetService.addMember(member2.getId(), meetId);
        meetService.deleteMember(member2.getId(), meetId);
        Meet meet = meetService.findOne(meetId);

        //then
        assertEquals(2, meet.getMemberMeets().size());
    }

}
