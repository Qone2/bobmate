package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.*;
import com.bobmate.bobmate.dto.CreateMemberDto;
import com.bobmate.bobmate.exception.HeadMemberException;
import com.bobmate.bobmate.exception.MemberMeetDuplicateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        Long meetId = meetService.saveMeet(member1.getId(), place.getId(), "모임1", "http://dfsf.c",
                5, LocalDateTime.now().plusWeeks(2));
        Meet meet = meetService.findOne(meetId);

        //then
        assertEquals(member1, meet.getHeadMember());
        assertEquals(place, meet.getPlace());
        assertEquals(1, meet.getMemberMeets().size());
        assertEquals(meet.getMemberMeets().size(), meet.getMemberCount());

        assertEquals(1, member1.getMemberMeets().size());

        assertEquals(meet.getMemberMeets().get(0), member1.getMemberMeets().get(0));

        assertEquals(1, place.getMeets().size());
        assertEquals(meet, place.getMeets().get(0));
    }

    @Test
    public void 멤버추가() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);
        CreateMemberDto memberDto2 = new CreateMemberDto("member2",
                passwordEncoder.encode("password2"),
                "nickname2", Collections.singletonList("ROLE_USER"));
        Long memberId2 = memberService.join(memberDto2);
        Member member2 = memberService.findOne(memberId2);
        CreateMemberDto memberDto3 = new CreateMemberDto("member3",
                passwordEncoder.encode("password3"),
                "nickname3", Collections.singletonList("ROLE_USER"));
        Long memberId3 = memberService.join(memberDto3);
        Member member3 = memberService.findOne(memberId3);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        Long meetId = meetService.saveMeet(member1.getId(), place.getId(), "모임0", "http://dfsf.c",
                5, LocalDateTime.now().plusWeeks(2));
        meetService.addMember(member2.getId(), meetId);
        meetService.addMember(member3.getId(), meetId);
        Meet meet = meetService.findOne(meetId);

        //then
        assertEquals(3, meet.getMemberMeets().size());
        assertEquals(meet.getMemberMeets().size(), meet.getMemberCount());
        assertEquals(member1.getMemberMeets().get(0).getMeet(), member2.getMemberMeets().get(0).getMeet());
        assertEquals(member1.getMemberMeets().get(0).getMeet(), member3.getMemberMeets().get(0).getMeet());

        assertEquals(1, place.getMeets().size());
        assertEquals(meet, place.getMeets().get(0));
    }

    @Test
    public void 멤버삭제() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);
        CreateMemberDto memberDto2 = new CreateMemberDto("member2",
                passwordEncoder.encode("password2"),
                "nickname2", Collections.singletonList("ROLE_USER"));
        Long memberId2 = memberService.join(memberDto2);
        Member member2 = memberService.findOne(memberId2);
        CreateMemberDto memberDto3 = new CreateMemberDto("member3",
                passwordEncoder.encode("password3"),
                "nickname3", Collections.singletonList("ROLE_USER"));
        Long memberId3 = memberService.join(memberDto3);
        Member member3 = memberService.findOne(memberId3);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        Long meetId = meetService.saveMeet(member1.getId(), place.getId(), "모임0", "http://dfsf.c",
                5, LocalDateTime.now().plusWeeks(2));
        meetService.addMember(member2.getId(), meetId);
        meetService.addMember(member3.getId(), meetId);
        meetService.deleteMember(member3.getId(), meetId);
        Meet meet = meetService.findOne(meetId);

        //then
        assertEquals(2, meet.getMemberMeets().size());
        assertEquals(meet.getMemberMeets().size(), meet.getMemberCount());
    }

    @Test
    public void 모임중복가입() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);
        CreateMemberDto memberDto2 = new CreateMemberDto("member2",
                passwordEncoder.encode("password2"),
                "nickname2", Collections.singletonList("ROLE_USER"));
        Long memberId2 = memberService.join(memberDto2);
        Member member2 = memberService.findOne(memberId2);
        CreateMemberDto memberDto3 = new CreateMemberDto("member3",
                passwordEncoder.encode("password3"),
                "nickname3", Collections.singletonList("ROLE_USER"));
        Long memberId3 = memberService.join(memberDto3);
        Member member3 = memberService.findOne(memberId3);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        Long meetId = meetService.saveMeet(member1.getId(), place.getId(), "모임0", "http://dfsf.c",
                5, LocalDateTime.now().plusWeeks(2));
        meetService.addMember(member2.getId(), meetId);
        meetService.addMember(member3.getId(), meetId);

        //then
        assertThrows(MemberMeetDuplicateException.class, () -> meetService.addMember(member2.getId(), meetId));
    }
    
    @Test
    public void 멤버삭제시_방장일경우() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);
        CreateMemberDto memberDto2 = new CreateMemberDto("member2",
                passwordEncoder.encode("password2"),
                "nickname2", Collections.singletonList("ROLE_USER"));
        Long memberId2 = memberService.join(memberDto2);
        Member member2 = memberService.findOne(memberId2);
        CreateMemberDto memberDto3 = new CreateMemberDto("member3",
                passwordEncoder.encode("password3"),
                "nickname3", Collections.singletonList("ROLE_USER"));
        Long memberId3 = memberService.join(memberDto3);
        Member member3 = memberService.findOne(memberId3);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        //when
        Long meetId = meetService.saveMeet(member1.getId(), place.getId(), "모임0", "http://dfsf.c",
                5, LocalDateTime.now().plusWeeks(2));
        meetService.addMember(member2.getId(), meetId);
        meetService.addMember(member3.getId(), meetId);

        //then
        meetService.deleteMember(member3.getId(), meetId);
        assertThrows(HeadMemberException.class, () -> meetService.deleteMember(member1.getId(), meetId));
    }

}
