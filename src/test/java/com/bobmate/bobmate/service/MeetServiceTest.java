package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MeetServiceTest {

    @Autowired MemberService memberService;
    @Autowired PlaceService placeService;
    @Autowired MeetService meetService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void 모임생성() throws Exception {
        //given
        Member member = new Member();
        member.setEmail("member0@member.com");
        memberService.join(member);

        Place place = new Place();
        place.setName("식당1");
        place.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place);

        //when
        Long meetId = meetService.saveMeet(member.getId(), place.getId(), "모임1", "http://dfsf.c");
        Meet meet = meetService.findOne(meetId);

        //then
        assertEquals(member, meet.getHeadMember());
        assertEquals(place, meet.getPlace());
        assertEquals(1, meet.getMemberMeets().size());

        assertEquals(1, member.getMemberMeets().size());

        assertEquals(meet.getMemberMeets().get(0), member.getMemberMeets().get(0));

        assertEquals(1, place.getMeets().size());
        assertEquals(meet, place.getMeets().get(0));
    }

    @Test
    public void 멤버추가() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member1);
        Member member2 = new Member();
        member2.setEmail("member2@member1.com");
        member2.setPassword(passwordEncoder.encode("password1"));
        member2.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member2);
        Member member3 = new Member();
        member3.setEmail("member3@member1.com");
        member3.setPassword(passwordEncoder.encode("password1"));
        member3.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member3);

        Place place = new Place();
        place.setName("식당0");
        place.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place);

        //when
        Long meetId = meetService.saveMeet(member1.getId(), place.getId(), "모임0", "http://dfsf.c");
        meetService.addMember(member2.getId(), meetId);
        meetService.addMember(member3.getId(), meetId);
        Meet meet = meetService.findOne(meetId);

        //then
        assertEquals(3, meet.getMemberMeets().size());
        assertEquals(member1.getMemberMeets().get(0).getMeet(), member2.getMemberMeets().get(0).getMeet());
        assertEquals(member1.getMemberMeets().get(0).getMeet(), member3.getMemberMeets().get(0).getMeet());

        assertEquals(1, place.getMeets().size());
        assertEquals(meet, place.getMeets().get(0));
    }

    @Test
    public void 멤버삭제() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("member1@member1.com");
        member1.setPassword(passwordEncoder.encode("password1"));
        member1.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member1);
        Member member2 = new Member();
        member2.setEmail("member2@member1.com");
        member2.setPassword(passwordEncoder.encode("password1"));
        member2.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member2);
        Member member3 = new Member();
        member3.setEmail("member3@member1.com");
        member3.setPassword(passwordEncoder.encode("password1"));
        member3.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member3);

        Place place = new Place();
        place.setName("식당0");
        place.setCoordinate(new Coordinate(123.123, 321.321));
        placeService.savePlace(place);

        //when
        Long meetId = meetService.saveMeet(member1.getId(), place.getId(), "모임0", "http://dfsf.c");
        meetService.addMember(member2.getId(), meetId);
        meetService.addMember(member3.getId(), meetId);
        meetService.deleteMember(member3.getId(), meetId);
        Meet meet = meetService.findOne(meetId);

        //then
        assertEquals(2, meet.getMemberMeets().size());
    }

}
