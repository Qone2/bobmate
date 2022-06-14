package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.*;
import com.bobmate.bobmate.dto.CreateMemberDto;
import com.bobmate.bobmate.exception.NicknameDuplicateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PlaceService placeService;
    @Autowired
    ReviewService reviewService;

    @Test
    public void 맴버삭제() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);

        //when
        assertEquals(memberId1, memberService.deleteMember(memberId1));

        //then
        assertEquals(MemberStatus.DELETED, member1.getMemberStatus());
    }

    @Test
    public void 관계를_맺은상태에서_맴버삭제() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);

        Long placeId = placeService.savePlace("식당1", new Coordinate(123.123, 321.321));
        Place place = placeService.findOne(placeId);

        String contents = "맛있다!";
        Long reviewId = reviewService.saveReview(memberId1, placeId, contents, 5.0, new ArrayList<>());

        Review review = reviewService.findOne(reviewId);

        //when
        assertEquals(memberId1, memberService.deleteMember(memberId1));

        //then
        assertEquals(MemberStatus.DELETED, member1.getMemberStatus());

    }

    @Test
    public void 닉네임중복() throws Exception {
        //given
        CreateMemberDto memberDto1 = new CreateMemberDto("member1",
                passwordEncoder.encode("password1"),
                "nickname1", Collections.singletonList("ROLE_USER"));
        Long memberId1 = memberService.join(memberDto1);
        Member member1 = memberService.findOne(memberId1);

        //when
        CreateMemberDto memberDto2 = new CreateMemberDto("member2",
                passwordEncoder.encode("password2"),
                "nickname1", Collections.singletonList("ROLE_USER"));

        //then
        assertThrows(NicknameDuplicateException.class, () -> memberService.join(memberDto2));

    }

}
