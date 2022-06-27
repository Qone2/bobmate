package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Coordinate;
import com.bobmate.bobmate.domain.Place;
import com.bobmate.bobmate.domain.PlaceStatus;
import com.bobmate.bobmate.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class PlaceApiController {

    private final PlaceService placeService;

    /**
     * 장소 등록
     */
    @PostMapping("/api/v1/place")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "장소 등록", description = "장소를 등록합니다. 중복검사 없음<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public CreatePlaceResponse savePlaceV1(@RequestBody @Valid CreatePlaceRequest createPlaceRequest) {

        Coordinate coordinate = new Coordinate(createPlaceRequest.getX(), createPlaceRequest.getY());

        Long id = placeService.savePlace(createPlaceRequest.getName(), coordinate);
        return new CreatePlaceResponse(id);
    }

    @Getter
    static class CreatePlaceRequest {
        @NotEmpty
        private String name;

        private Double x;
        private Double y;
    }

    @Getter
    @AllArgsConstructor
    static class CreatePlaceResponse {
        private Long place_id;
    }


    /**
     * 모든 장소 조회
     */
    @GetMapping("/api/v1/place")
    @Operation(summary = "모든 장소 조회", description = "전체 장소를 조회합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public Result placesV1() {
        List<PlaceDto> collect = placeService.findAll()
                .stream().map(p -> new PlaceDto(p))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Getter
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Getter
    @AllArgsConstructor
    static class PlaceDto {
        private Long place_id;
        private String name;
        private Coordinate coordinate;
        private int review_count;
        private Double avg_star;
        private List<Long> review_ids;
        private List<Long> meet_ids;
        private PlaceStatus placeStatus;

        public PlaceDto(Place place) {
            this.place_id = place.getId();
            this.name = place.getName();
            this.coordinate = place.getCoordinate();
            this.review_count = place.getReviewCount();
            this.avg_star = place.getAvgStar();
            this.review_ids = place.getReviews().stream().map(r -> r.getId()).collect(Collectors.toList());
            this.meet_ids = place.getMeets().stream().map(m -> m.getId()).collect(Collectors.toList());
            this.placeStatus = place.getPlaceStatus();
        }
    }

    /**
     * 장소 상세 조회
     */
    @GetMapping("/api/v1/place/{place_id}")
    @Operation(summary = "장소 상세 조회", description = "장소의 상세정보를 조회합니다. schema를 누르면 추가설명.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public PlaceDetailResponse placeDetailV1(@PathVariable("place_id") Long place_id) {
        Place place = placeService.findOne(place_id);
        return new PlaceDetailResponse(place);
    }

    @Getter
    @AllArgsConstructor
    static class PlaceDetailResponse {
        private Long place_id;
        private String name;
        @Schema(description = "좌표")
        private Coordinate coordinate;
        @Schema(description = "이 장소에 연결된 리뷰 id리스트")
        private List<Long> review_ids;
        @Schema(description = "이 장소에 연결된 소모임 id리스트")
        private List<Long> meet_ids;
        @Schema(description = "이 장소에 연결된 리뷰 수")
        private int review_count;
        @Schema(description = "이 장소에 연결된 리뷰의 평균 별점")
        private Double avg_star;
        @Schema(description = "장소의 상태 (VALID, DELETED")
        private PlaceStatus placeStatus;

        public PlaceDetailResponse(Place place) {
            this.place_id = place.getId();
            this.name = place.getName();
            this.coordinate = place.getCoordinate();
            this.review_ids = place.getReviews().stream().map(r -> r.getId()).collect(Collectors.toList());
            this.meet_ids = place.getMeets().stream().map(m -> m.getId()).collect(Collectors.toList());
            this.review_count = place.getReviewCount();
            this.avg_star = place.getAvgStar();
            this.placeStatus = place.getPlaceStatus();
        }
    }

    /**
     * 장소 삭제
     */
    @DeleteMapping("/api/v1/place/{place_id}")
    @Operation(summary = "장소 삭제 (논리삭제)", description = "장소를 삭제합니다.<br>" +
            "물리적 삭제가 아니라 논리삭제로 진행되어 장소엔티티의 place status 값이 VALID에서 DELETED로 변경됩니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "422 : 장소가 이미 삭제된 경우<br>" +
            "500 : 내부 서버 에러")
    public DeletePlaceResponse deletePlace(@PathVariable Long place_id) {
        return new DeletePlaceResponse(placeService.deletePlace(place_id));
    }

    @Getter
    @AllArgsConstructor
    static class DeletePlaceResponse {
        private Long place_id;
    }

    /**
     * 장소 정규식으로 검색
     */
    @GetMapping("/api/v1/place-search")
    @Operation(summary = "이름으로 장소 검색(정규식)", description = "이름으로 장소를 검색합니다.<br>" +
            "정규표현식을 사용할 수 있어 그냥 \"식당\"이런 식으로 검색도 가능하고\"식[다-딯]\"이런 검색도 가능합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "500 : 내부 서버 에러")
    public Result placeSearchRegex(@RequestParam String search) {
        List<PlaceDto> placeDtoList = placeService.findAllByName(search)
                .stream().map(p -> new PlaceDto(p)).collect(Collectors.toList());
        return new Result(placeDtoList.size(), placeDtoList);
    }
}
