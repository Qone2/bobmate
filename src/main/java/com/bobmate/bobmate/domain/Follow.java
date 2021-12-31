package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(
        uniqueConstraints = {
              @UniqueConstraint(name = "CantFollowTwice", columnNames = {"from_member_id", "to_member_id"})
        }
)
public class Follow {

    @Id
    @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;


    //==연관관계 메서드==//
    public void setFromMember(Member member) {
        this.fromMember = member;
        member.getFollowing().add(this);
    }

    public void setToMember(Member member) {
        this.toMember = member;
        member.getFollowers().add(this);
    }


    //==생성 메서드==//
    public static Follow createFollow(Member fromMember, Member toMember) {
        Follow follow = new Follow();
        follow.setFromMember(fromMember);
        follow.setToMember(toMember);

        return follow;
    }
}
