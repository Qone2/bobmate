package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Place {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private String name;

    @Embedded
    private Coordinate coordinate;

    @OneToMany(mappedBy = "place")
    private List<Review> reviews = new ArrayList<>();

    private int reviewCount;

    private Double avgStar;

    //==생성 메서드==//
//    public static Place createPlace(Place place) {
//        Place place1 = new Place();
//        place1.setName(place.getName());
//        place1.setCoordinate(place.getCoordinate());
//        place1.setReviewCount(0);
//        return place1;
//    }

    //==비즈니스 로직==//
    /**
     * 평균 별점 계산
     */
    public void updateAvgStar() {
        if (this.reviewCount == 0) {
            setAvgStar((double) 0);
            return;
        }
        int totalStar = 0;
        for (Review review : this.reviews) {
            if (review.getReviewStatus() == ReviewStatus.VALID) {
                totalStar += review.getStar();
            }
        }
        setAvgStar((double) totalStar / this.reviewCount);
    }

    public void addReviewCount() {
        this.reviewCount += 1;
    }

    public void subtractReviewCount() {
        this.reviewCount -= 1;
    }
}
