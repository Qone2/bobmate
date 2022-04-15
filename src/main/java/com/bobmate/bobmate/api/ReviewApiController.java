package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Photo;
import com.bobmate.bobmate.domain.Review;
import com.bobmate.bobmate.domain.ReviewStatus;
import com.bobmate.bobmate.handler.PhotoHandler;
import com.bobmate.bobmate.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/api/v1/review")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("리뷰 등록")
    public CreateReviewResponse saveReviewV1(@Valid CreateReviewRequest request) {
        List<Photo> photoList = photoHandler.parseFileInfo(request.getPhotos());
        Long id = reviewService.saveReview(request.getMember_id(), request.getPlace_id(),
                request.getContent(), request.getStar(), photoList);
        return new CreateReviewResponse(id);
    }

    @Data
    static class CreateReviewRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private Long place_id;
        @NotEmpty
        private String content;
        @NotNull
        @Max(value = 5) @Min(value = 1)
        private Double star;
        private List<MultipartFile> photos;
    }

    @Data
    @AllArgsConstructor
    static class CreateReviewResponse {
        private Long review_id;
    }

    /**
     * 전체 리뷰 조회
     */
    @GetMapping("/api/v1/review")
    @ApiOperation("전체 리뷰 조회")
    public Result reviewsV1() {
        List<Review> reviews = reviewService.findAll();
        List<ReviewDto> collect = reviews.stream()
                .map(r -> new ReviewDto(r.getId(), r.getMember().getId(), r.getPlace().getId(),
                        r.getPhotos().stream().map(p -> p.getId()).collect(Collectors.toList()), r.getStar(),
                        r.getCreatedDate(), r.getUpdatedDate(), r.getLikeCount(), r.getReviewStatus()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
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

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    /**
     * 리뷰 상세 조회
     */
    @GetMapping("/api/v1/review/{id}")
    @ApiOperation("리뷰 상세 조회")
    public ReviewDetailResponse reviewDetailV1(@PathVariable("id") Long id) {
        Review review = reviewService.findOne(id);
        return new ReviewDetailResponse(review.getId(), review.getMember().getId(), review.getPlace().getId(),
                review.getPhotos().stream().map(p -> p.getId()).collect(Collectors.toList()), review.getContent(),
                review.getStar(), review.getCreatedDate(), review.getUpdatedDate(), review.getLikeCount(),
                review.getReviewStatus());
    }

    @Data
    @AllArgsConstructor
    static class ReviewDetailResponse {
        private Long review_id;
        private Long member_id;
        private Long place_id;
        private List<Long> photo_ids;
        private String content;
        private Double star;
        private LocalDateTime created_date;
        private LocalDateTime updated_date;
        private int like_count;
        private ReviewStatus review_status;
    }
}
