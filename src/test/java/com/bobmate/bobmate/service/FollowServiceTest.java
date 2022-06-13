package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.dto.CreateMemberDto;
import com.bobmate.bobmate.exception.FollowDuplicateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FollowServiceTest {

    @Autowired FollowService followService;
    @Autowired MemberService memberService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void 팔로우() throws Exception {
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
        CreateMemberDto memberDto4 = new CreateMemberDto("member4",
                passwordEncoder.encode("password4"),
                "nickname4", Collections.singletonList("ROLE_USER"));
        Long memberId4 = memberService.join(memberDto4);
        Member member4 = memberService.findOne(memberId4);

        //when
        followService.follow(member1.getId(), member2.getId());
        followService.follow(member1.getId(), member3.getId());
        followService.follow(member4.getId(), member1.getId());

        //then
        assertEquals(1, member1.getFollowers().size());
        assertEquals(2, member1.getFollowing().size());
    }


    @Test
    public void 언팔() throws Exception {
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
        CreateMemberDto memberDto4 = new CreateMemberDto("member4",
                passwordEncoder.encode("password4"),
                "nickname4", Collections.singletonList("ROLE_USER"));
        Long memberId4 = memberService.join(memberDto4);
        Member member4 = memberService.findOne(memberId4);

        //when
        followService.follow(member1.getId(), member2.getId());
        followService.follow(member1.getId(), member3.getId());
        followService.follow(member4.getId(), member1.getId());
        followService.unfollow(member1.getId(), member3.getId());
        Member findMember = memberService.findOne(member1.getId());

        //then
        assertEquals(1, findMember.getFollowing().size());
        assertEquals(1, findMember.getFollowers().size());

    }

    @Test
    public void 팔로우_목록() throws Exception {
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
        CreateMemberDto memberDto4 = new CreateMemberDto("member4",
                passwordEncoder.encode("password4"),
                "nickname4", Collections.singletonList("ROLE_USER"));
        Long memberId4 = memberService.join(memberDto4);
        Member member4 = memberService.findOne(memberId4);

        //when
        followService.follow(member1.getId(), member2.getId());
        followService.follow(member1.getId(), member3.getId());
        followService.follow(member4.getId(), member1.getId());

        //then
        assertEquals(member2.getFollowers().get(0), member1.getFollowing().get(0));
        assertEquals(member3.getFollowers().get(0), member1.getFollowing().get(1));
        assertEquals(member4.getFollowing().get(0), member1.getFollowers().get(0));

        assertEquals(member2, member1.getFollowing().get(0).getToMember());
        assertEquals(member3, member1.getFollowing().get(1).getToMember());
        assertEquals(member1, member1.getFollowing().get(0).getFromMember());
        assertEquals(member4, member1.getFollowers().get(0).getFromMember());

    }

    @Test
    public void 팔로우중복() throws Exception {
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
        CreateMemberDto memberDto4 = new CreateMemberDto("member4",
                passwordEncoder.encode("password4"),
                "nickname4", Collections.singletonList("ROLE_USER"));
        Long memberId4 = memberService.join(memberDto4);
        Member member4 = memberService.findOne(memberId4);

        //when
        followService.follow(member1.getId(), member2.getId());
        followService.follow(member1.getId(), member3.getId());
        followService.follow(member4.getId(), member1.getId());

        //then
        assertThrows(FollowDuplicateException.class, () -> followService.follow(member1.getId(), member2.getId()));
    }

}
