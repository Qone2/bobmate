package com.bobmate.bobmate;

import com.bobmate.bobmate.domain.*;
import com.bobmate.bobmate.service.MeetService;
import com.bobmate.bobmate.service.MemberService;
import com.bobmate.bobmate.service.PlaceService;
import com.bobmate.bobmate.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class initDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final MemberService memberService;
        private final PlaceService placeService;
        private final ReviewService reviewService;
        private final MeetService meetService;
        private final PasswordEncoder passwordEncoder;

        public void dbInit1() {
            Member member1 = new Member();
            member1.setEmail("member1@member.com");
            member1.setPassword(passwordEncoder.encode("password1"));
            member1.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member1);
            Member member2 = new Member();
            member2.setEmail("member2@member.com");
            member2.setPassword(passwordEncoder.encode("password1"));
            member2.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member2);
            Member member3 = new Member();
            member3.setEmail("member3@member.com");
            member3.setPassword(passwordEncoder.encode("password1"));
            member3.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member3);
            Member member4 = new Member();
            member4.setEmail("member4@member.com");
            member4.setPassword(passwordEncoder.encode("password1"));
            member4.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member4);
            Member member5 = new Member();
            member5.setEmail("member5@member.com");
            member5.setPassword(passwordEncoder.encode("password1"));
            member5.setRoles(Collections.singletonList("ROLE_USER"));
            memberService.join(member5);

            Place place1 = new Place();
            place1.setCoordinate(new Coordinate(123.123, 321.321));
            place1.setName("식당1");
            placeService.savePlace(place1);
            Place place2 = new Place();
            place2.setCoordinate(new Coordinate(13.123, 31.321));
            place2.setName("식당2");
            placeService.savePlace(place2);
            Place place3 = new Place();
            place3.setCoordinate(new Coordinate(12.123, 32.321));
            place3.setName("식당3");
            placeService.savePlace(place3);

            reviewService.saveReview(member1.getId(), place1.getId(), "리뷰내용1", 1.5);
            reviewService.saveReview(member1.getId(), place1.getId(), "리뷰내용2", 2.5);
            reviewService.saveReview(member1.getId(), place2.getId(), "리뷰내용3", 3.5);
            reviewService.saveReview(member2.getId(), place2.getId(), "리뷰내용4", 4.5);
            reviewService.saveReview(member3.getId(), place3.getId(), "리뷰내용5", 3.5);

            Long meetId1 = meetService.saveMeet(member1.getId(), place3.getId(), "모임1", "링크1");
            meetService.addMember(member4.getId(), meetId1);
            meetService.addMember(member5.getId(), meetId1);
        }
    }
}
