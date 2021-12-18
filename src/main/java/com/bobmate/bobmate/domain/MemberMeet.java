package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "NoSameMember", columnNames = {"member_id", "meet_id"})
        }
)
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
    public static MemberMeet createMemberMeet(Member member) {
        MemberMeet memberMeet = new MemberMeet();
        memberMeet.setMember(member);
        memberMeet.setJoinDate(LocalDateTime.now());

        return memberMeet;
    }

    //==비즈니스 로직==//
}
