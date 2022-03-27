package com.bobmate.bobmate.service;

import com.bobmate.bobmate.domain.Member;
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
        Member member4 = new Member();
        member4.setEmail("member4@member1.com");
        member4.setPassword(passwordEncoder.encode("password1"));
        member4.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member4);

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
        Member member4 = new Member();
        member4.setEmail("member4@member1.com");
        member4.setPassword(passwordEncoder.encode("password1"));
        member4.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member4);

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
        Member member4 = new Member();
        member4.setEmail("member4@member1.com");
        member4.setPassword(passwordEncoder.encode("password1"));
        member4.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member4);

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
        Member member4 = new Member();
        member4.setEmail("member4@member1.com");
        member4.setPassword(passwordEncoder.encode("password1"));
        member4.setRoles(Collections.singletonList("ROLE_USER"));
        memberService.join(member4);

        //when
        followService.follow(member1.getId(), member2.getId());
        followService.follow(member1.getId(), member3.getId());
        followService.follow(member4.getId(), member1.getId());

        //then
        assertThrows(FollowDuplicateException.class, () -> followService.follow(member1.getId(), member2.getId()));
    }

}
