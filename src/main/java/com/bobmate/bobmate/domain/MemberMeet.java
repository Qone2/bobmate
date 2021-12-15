package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class MemberMeet {

    @Id
    @GeneratedValue
    @Column(name = "member_meet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meet_id")
    private Meet meet;

    private LocalDateTime joinDate;

    //==생성 메서드==//
    public static MemberMeet createMemberMeet(Member member, Meet meet) {
        MemberMeet memberMeet = new MemberMeet();
        memberMeet.setMember(member);
        memberMeet.setMeet(meet);
        memberMeet.setJoinDate(LocalDateTime.now());

        return memberMeet;
    }
}
