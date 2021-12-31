package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "CantBookmarkTwice", columnNames = {"member_id", "place_id"})
        }
)
public class Bookmark {

    @Id
    @GeneratedValue
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    private LocalDateTime bookmarkedDate;


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getBookmarks().add(this);
    }


    //==생성 메서드==//
    public static Bookmark createBookmark(Member member, Place place) {
        Bookmark bookmark = new Bookmark();
        bookmark.setMember(member);
        bookmark.setPlace(place);
        bookmark.setBookmarkedDate(LocalDateTime.now());

        return bookmark;
    }

}
