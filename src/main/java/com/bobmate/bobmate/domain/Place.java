package com.bobmate.bobmate.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(indexes = @Index(name = "p_name", columnList = "name"))
public class Place {

    @Id @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private String name;

    @Embedded
    private Coordinate coordinate;

    @OneToMany(mappedBy = "place")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "place")
    private List<Meet> meets = new ArrayList<>();

    private int reviewCount;

    private Double avgStar;

    private PlaceStatus placeStatus;

    //==생성 메서드==//
    public static Place createPlace(String name, Coordinate coordinate) {
        Place place = new Place();
        place.setName(name);
        place.setCoordinate(coordinate);
        place.setReviewCount(0);
        place.setAvgStar((double) 0);
        place.setPlaceStatus(PlaceStatus.VALID);
        return place;
    }

    //==비즈니스 로직==//
    /**
     * 평균 별점 계산
     */
    public void updateAvgStar() {
        if (this.reviewCount == 0) {
            setAvgStar((double) 0);
            return;
        }
        Double totalStar = 0.0;
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

    /**
     * 장소 삭제
     * 논리적 삭제
     */
    public void delete() {
        setPlaceStatus(PlaceStatus.DELETED);
    }
}
