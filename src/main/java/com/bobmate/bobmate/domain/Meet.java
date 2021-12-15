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

    @OneToMany(mappedBy = "member")
    private List<Member> members = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member headMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private String link;

    private LocalDateTime createdDate;

    //==연관관계 메서드==//
    public void setHeadMember(Member headMember) {
        this.headMember = headMember;
        headMember.getMeets().add(this);
        this.members.add(headMember);
    }

    public void setPlace(Place place) {
        this.place = place;
        place.getMeets().add(this);
    }

    //==생성 메서드==//
    public static Meet createMeet(Member headMember, Place place, String name, String link) {
        Meet meet = new Meet();
        meet.setHeadMember(headMember);
        meet.setPlace(place);
        meet.setName(name);
        meet.setLink(link);
        meet.setCreatedDate(LocalDateTime.now());

        return meet;
    }
}
