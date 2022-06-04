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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_member_id")
    private Member headMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(mappedBy = "meet", cascade = CascadeType.ALL)
    private List<MemberMeet> memberMeets = new ArrayList<>();

    private String name;

    private String link;

    private int memberCount;

    private LocalDateTime createdDate;

    //==연관관계 메서드==//
    public void setPlace(Place place) {
        this.place = place;
        place.getMeets().add(this);
    }

    public void setMemberCount() {
        this.memberCount = this.memberMeets.size();
    }

    //==생성 메서드==//
    public static Meet createMeet(Member headMember, Place place, String name, String link) {
        Meet meet = new Meet();
        meet.setHeadMember(headMember);
        meet.setPlace(place);
        meet.setName(name);
        meet.setLink(link);
        meet.setCreatedDate(LocalDateTime.now());
        meet.setMemberCount();

        return meet;
    }

    //==비즈니스 로직==//
}
