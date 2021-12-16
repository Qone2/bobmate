package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Meet {

    @Id
    @GeneratedValue
    @Column(name = "meet_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "meet")
    private List<MemberMeet> memberMeets = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member headMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private String link;

    private LocalDateTime createdDate;

    //==연관관계 메서드==//
    public void setHeadMember(Member headMember, MemberMeet memberMeet) {
        this.headMember = headMember;
        headMember.getMemberMeets().add(memberMeet);
    }

    public void setPlace(Place place) {
        this.place = place;
        place.getMeets().add(this);
    }

    public void addMemberMeets(MemberMeet memberMeet) {
        this.memberMeets.add(memberMeet);
        memberMeet.setMeet(this);
    }

    //==생성 메서드==//
    public static Meet createMeet(Member headMember, MemberMeet memberMeet, Place place, String name, String link) {
        Meet meet = new Meet();
        meet.setHeadMember(headMember, memberMeet);
        meet.setPlace(place);
        meet.setName(name);
        meet.setLink(link);
        meet.addMemberMeets(memberMeet);
        meet.setCreatedDate(LocalDateTime.now());

        return meet;
    }

    //==비즈니스 로직==//
    public void addMember(MemberMeet memberMeet) {
        this.memberMeets.add(memberMeet);
        memberMeet.setMeet(this);
        memberMeet.getMember().getMemberMeets().add(memberMeet);
    }
}
