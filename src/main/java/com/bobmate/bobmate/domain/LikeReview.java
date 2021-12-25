package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "Can't like twice", columnNames = {"member_id", "review_id"})
})
public class LikeReview {

    @Id
    @GeneratedValue
    @Column(name = "like_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private LocalDateTime likeDate;


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getLikeReviews().add(this);
    }


    //==생성 메서드==//
    public static LikeReview createLikeReview(Member member, Review review) {
        LikeReview likeReview = new LikeReview();
        likeReview.setMember(member);
        likeReview.setReview(review);
        likeReview.setLikeDate(LocalDateTime.now());

        return likeReview;
    }
}
