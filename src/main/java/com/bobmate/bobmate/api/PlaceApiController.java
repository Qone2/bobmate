package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.service.PlaceService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class PlaceApiController {

    private final PlaceService placeService;

    @PostMapping("/api/v1/place")
    public CreatePlaceResponse savePlaceV1(@RequestBody @Valid CreatePlaceRequest createPlaceRequest) {

        Coordinate coordinate = new Coordinate(createPlaceRequest.getX(), createPlaceRequest.getY());
        Place place = new Place();
        place.setName(createPlaceRequest.getName());
        place.setCoordinate(coordinate);

        Long id = placeService.savePlace(place);
        return new CreatePlaceResponse(id);
    }

    @Data
    static class CreatePlaceRequest {
        @NotEmpty
        private String name;

        private Double x;
        private Double y;
    }

    @Data
    @AllArgsConstructor
    static class CreatePlaceResponse {
        private Long place_id;
    }
}
