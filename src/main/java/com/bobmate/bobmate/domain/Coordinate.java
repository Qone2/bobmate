package com.bobmate.bobmate.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Coordinate {

    private Double x;
    private Double y;

    public Coordinate() {
    }

    public Coordinate(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}
