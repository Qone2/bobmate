package com.bobmate.bobmate.api;

import com.bobmate.bobmate.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @PostMapping("/api/v1/review")
    public CreateReviewResponse saveReviewV1(@RequestBody @Valid CreateReviewRequest createReviewRequest) {
        Long id = reviewService.saveReview(createReviewRequest.getMemberId(), createReviewRequest.getPlaceId(),
                createReviewRequest.getContents(), createReviewRequest.getStar());
        return new CreateReviewResponse(id);
    }

    @Data
    static class CreateReviewRequest {
        @NotNull
        private Long memberId;
        @NotNull
        private Long placeId;
        @NotEmpty
        private String contents;
        @NotNull
        @Max(value = 5) @Min(value = 1)
        private Double star;
    }

    @Data
    @AllArgsConstructor
    static class CreateReviewResponse {
        private Long reviewId;
    }
}
