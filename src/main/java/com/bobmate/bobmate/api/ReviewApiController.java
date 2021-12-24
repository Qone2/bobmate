package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Review;
import com.bobmate.bobmate.domain.ReviewStatus;
import com.bobmate.bobmate.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @PostMapping("/api/v1/review")
    public CreateReviewResponse saveReviewV1(@RequestBody @Valid CreateReviewRequest createReviewRequest) {
        Long id = reviewService.saveReview(createReviewRequest.getMember_id(), createReviewRequest.getPlace_id(),
                createReviewRequest.getContents(), createReviewRequest.getStar());
        return new CreateReviewResponse(id);
    }

    @Data
    static class CreateReviewRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private Long place_id;
        @NotEmpty
        private String contents;
        @NotNull
        @Max(value = 5) @Min(value = 1)
        private Double star;
    }

    @Data
    @AllArgsConstructor
    static class CreateReviewResponse {
        private Long review_id;
    }

    @GetMapping("/api/v1/review")
    public Result reviewsV1() {
        List<Review> reviews = reviewService.findAll();
        List<ReviewDto> collect = reviews.stream()
                .map(r -> new ReviewDto(r.getId(), r.getMember().getId(), r.getPlace().getId(),
                        r.getStar(), r.getCreatedDate(), r.getUpdatedDate(), r.getLikeCount(),
                        r.getReviewStatus()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class ReviewDto {
        private Long review_id;
        private Long member_id;
        private Long place_id;
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

    @GetMapping("/api/v1/review/{id}")
    public ReviewDetailResponse reviewDetailV1(@PathVariable("id") Long id) {
        Review review = reviewService.findOne(id);
        return new ReviewDetailResponse(review.getId(), review.getMember().getId()
                , review.getPlace().getId(), review.getContents(), review.getStar(), review.getCreatedDate()
                , review.getUpdatedDate(), review.getLikeCount(), review.getReviewStatus());
    }

    @Data
    @AllArgsConstructor
    static class ReviewDetailResponse {
        private Long review_id;
        private Long member_id;
        private Long place_id;
        private String content;
        private Double star;
        private LocalDateTime created_date;
        private LocalDateTime updated_date;
        private int like_count;
        private ReviewStatus review_status;
    }
}
