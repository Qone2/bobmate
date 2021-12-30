package com.bobmate.bobmate.api;

import com.bobmate.bobmate.service.LikeReviewService;
import com.bobmate.bobmate.service.MemberService;
import com.bobmate.bobmate.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class LikeReviewApiController {

    private final LikeReviewService likeReviewService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @PostMapping("/api/v1/like-review")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeReviewResponse likeReview(@RequestBody @Valid LikeReviewRequest request) {
        Long likeReviewId =  likeReviewService.likeReview(request.getMember_id(), request.getReview_id());
        return new LikeReviewResponse(likeReviewId);
    }

    @Data
    @AllArgsConstructor
    static class LikeReviewResponse {
        private Long like_review_id;
    }

    @Data
    static class LikeReviewRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private Long review_id;
    }


    @DeleteMapping("/api/v1/unlike-review")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UnlikeReviewResponse unlikeReview(@RequestBody @Valid UnlikeReviewRequest request) {
        likeReviewService.unlikeReview(request.getMember_id(), request.getReview_id());
        return new UnlikeReviewResponse("done!");
    }

    @Data
    @AllArgsConstructor
    static class UnlikeReviewResponse {
        private String massage;
    }

    @Data
    static class UnlikeReviewRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private Long review_id;
    }
}
