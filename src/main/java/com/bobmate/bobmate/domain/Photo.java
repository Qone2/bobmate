package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter
public class Photo {

    @Id
    @GeneratedValue
    @Column(name = "photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @NotNull
    private String fileName;

    @NotNull
    private String filePath;

    private Long fileSize;


    //==연관관계 메서드==//
    public void setReview(Review review) {
        this.review = review;
        review.getPhotos().add(this);
    }
}
