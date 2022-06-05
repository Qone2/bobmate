package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Photo;
import com.bobmate.bobmate.domain.Review;
import com.bobmate.bobmate.domain.ReviewStatus;
import com.bobmate.bobmate.handler.PhotoHandler;
import com.bobmate.bobmate.service.ReviewService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰 관련
 */
@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;
    private final PhotoHandler photoHandler;

    /**
     * 리뷰 등록
     */
    @PostMapping(value = "/api/v1/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "리뷰 등록", description = "리뷰를 등록합니다.<br>" +
            "사진은 필수가 아니며, request body(json)가 아닌 form-data형식으로 보내야 합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "400 : 이미 삭제된 장소일 경우<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public CreateReviewResponse saveReviewV1(@ModelAttribute @Valid CreateReviewRequest request) {
        List<Photo> photoList = photoHandler.parseFileInfo(request.getPhotos());
        Long id = reviewService.saveReview(request.getMember_id(), request.getPlace_id(),
                request.getContent(), request.getStar(), photoList);
        return new CreateReviewResponse(id);
    }

    @Getter
    static class CreateReviewRequest {
        @NotNull
        @ApiParam(value = "리뷰를 작성한 멤버", required = true)
        private Long member_id;
        @NotNull
        @ApiParam(value = "리뷰의 대상이 되는 장소", required = true)
        private Long place_id;
        @NotEmpty
        @ApiParam(value = "후기 내용", required = true)
        private String content;
        @NotNull
        @Max(value = 5) @Min(value = 1)
        @ApiParam(value = "별점 1 ~ 5점", required = true)
        private Double star;
        @ApiParam(value = "사진파일(jpg, jpeg, png, heic, heif만 허용됩니다.)")
        private List<MultipartFile> photos;
    }

    @Getter
    @AllArgsConstructor
    static class CreateReviewResponse {
        private Long review_id;
    }

    /**
     * 전체 리뷰 조회
     */
    @GetMapping("/api/v1/review")
    @Operation(summary = "전체 리뷰 조회", description = "전체 리뷰들을 조회합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public Result reviewsV1() {
        List<Review> reviews = reviewService.findAll();
        List<ReviewDto> collect = reviews.stream()
                .map(r -> new ReviewDto(r.getId(), r.getMember().getId(), r.getPlace().getId(),
                        r.getPhotos().stream().map(p -> p.getId()).collect(Collectors.toList()), r.getStar(),
                        r.getCreatedDate(), r.getUpdatedDate(), r.getLikeCount(), r.getReviewStatus()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Getter
    @AllArgsConstructor
    static class ReviewDto {
        private Long review_id;
        private Long member_id;
        private Long place_id;
        private List<Long> photo_ids;
        private Double star;
        private LocalDateTime created_date;
        private LocalDateTime updated_date;
        private int like_count;
        private ReviewStatus review_status;
    }

    @Getter
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    /**
     * 리뷰 상세 조회
     */
    @GetMapping("/api/v1/review/{review_id}")
    @Operation(summary = "리뷰 상세 조회", description = "리뷰의 상세정보를 조회합니다. schema를 누르면 추가설명<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public ReviewDetailResponse reviewDetailV1(@PathVariable("review_id") Long review_id) {
        Review review = reviewService.findOne(review_id);
        return new ReviewDetailResponse(review.getId(), review.getMember().getId(), review.getPlace().getId(),
                review.getPhotos().stream().map(p -> p.getId()).collect(Collectors.toList()), review.getContent(),
                review.getStar(), review.getCreatedDate(), review.getUpdatedDate(), review.getLikeCount(),
                review.getReviewStatus());
    }

    @Getter
    @AllArgsConstructor
    static class ReviewDetailResponse {
        @Schema(description = "리뷰 id")
        private Long review_id;
        @Schema(description = "리뷰를 작성한 멤버")
        private Long member_id;
        @Schema(description = "리뷰의 대상이 되는 장소")
        private Long place_id;
        @Schema(description = "리뷰에 들어가는 사진들 id리스트")
        private List<Long> photo_ids;
        @Schema(description = "리뷰 내용")
        private String content;
        @Schema(description = "별점")
        private Double star;
        @Schema(description = "생성 날짜")
        private LocalDateTime created_date;
        @Schema(description = "최근 수정된 날짜")
        private LocalDateTime updated_date;
        @Schema(description = "좋아요 수")
        private int like_count;
        @Schema(description = "리뷰 상태(VALID, DELETED")
        private ReviewStatus review_status;
    }

    /**
     * 리뷰삭제 (논리 삭제)
     */
    @DeleteMapping("/api/v1/review/{review_id}")
    @Operation(summary = "리뷰삭제", description = "리뷰를 삭제합니다. 물리삭제가 아닌 논리삭제로 진행됩니다.<br>" +
            "review status가 VALID에서 DELETED로 변경<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public DeleteReviewResponse deleteReviewV1(@PathVariable Long review_id) {
        return new DeleteReviewResponse(reviewService.deleteReview(review_id));
    }

    @Getter
    @AllArgsConstructor
    static class DeleteReviewResponse {
        private Long review_id;
    }
}
