package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class TagBookmark {

    @Id
    @GeneratedValue
    @Column(name = "tag_bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getTagBookmarks().add(this);
    }


    //==생성 메서드==//
    public static TagBookmark createTagBookmark(Tag tag, Bookmark bookmark, Member member) {
        TagBookmark tagBookmark = new TagBookmark();
        tagBookmark.setTag(tag);
        tagBookmark.setBookmark(bookmark);
        tagBookmark.setMember(member);

        return tagBookmark;
    }
}
