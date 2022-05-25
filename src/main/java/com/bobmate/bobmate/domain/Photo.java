package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    private LocalDateTime savedDate;


    //==연관관계 메서드==//
    public void setReview(Review review) {
        this.review = review;
        review.getPhotos().add(this);
    }


    //==생성 메서드==//
    public static Photo createPhoto(Review review, String fileName, String filePath, Long fileSize) {
        Photo photo = new Photo();
        photo.setReview(review);
        photo.setFileName(fileName);
        photo.setFilePath(filePath);
        photo.setFileSize(fileSize);
        photo.setSavedDate(LocalDateTime.now());

        return photo;
    }

    public static Photo photoHandle(String fileName, String filePath, Long fileSize) {
        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setFilePath(filePath);
        photo.setFileSize(fileSize);
        photo.setSavedDate(LocalDateTime.now());

        return photo;
    }
}
