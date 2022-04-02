package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "NoSameMember", columnNames = {"tag_id", "bookmark_id"})
        }
)
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


    //==연관관계 메서드==//

    //==생성 메서드==//
    public static TagBookmark createTagBookmark(Tag tag, Bookmark bookmark) {
        TagBookmark tagBookmark = new TagBookmark();
        tagBookmark.setTag(tag);
        tagBookmark.setBookmark(bookmark);

        return tagBookmark;
    }
}
