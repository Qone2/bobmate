package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.service.PlaceService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 장소 관련
 */
@RestController
@RequiredArgsConstructor
public class PlaceApiController {

    private final PlaceService placeService;

    /**
     * 장소 등록
     */
    @PostMapping("/api/v1/place")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("장소 등록")
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


    /**
     * 모든 장소 조회
     */
    @GetMapping("/api/v1/place")
    @ApiOperation("모든 장소 조회")
    public Result placesV1() {
        List<PlaceDto> collect = placeService.findAll()
                .stream().map(p -> new PlaceDto(p.getId(), p.getName(), p.getReviewCount(), p.getAvgStar(),
                        p.getReviews().stream().map(r -> r.getId()).collect(Collectors.toList()),
                        p.getMeets().stream().map(m -> m.getId()).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class PlaceDto {
        private Long place_id;
        private String name;
        private int review_count;
        private Double avg_star;
        private List<Long> review_ids;
        private List<Long> meet_ids;
    }

    /**
     * 장소 상세 조회
     */
    @GetMapping("/api/v1/place/{id}")
    @ApiOperation("장소 상세 조회")
    public PlaceDetailResponse placeDetailV1(@PathVariable("id") Long id) {
        Place place = placeService.findOne(id);
        return new PlaceDetailResponse(place.getId(), place.getName(), place.getCoordinate(),
                place.getReviews().stream().map(r -> r.getId()).collect(Collectors.toList()),
                place.getMeets().stream().map(m -> m.getId()).collect(Collectors.toList()),
                place.getReviewCount(), place.getAvgStar());
    }

    @Data
    @AllArgsConstructor
    static class PlaceDetailResponse {
        private Long place_id;
        private String name;
        private Coordinate coordinate;
        private List<Long> review_ids;
        private List<Long> meet_ids;
        private int review_count;
        private Double avg_star;
    }
}
