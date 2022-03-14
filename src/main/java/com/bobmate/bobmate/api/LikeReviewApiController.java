package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.LikeReview;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰 좋아요 관련
 */
@RestController
@RequiredArgsConstructor
public class LikeReviewApiController {

    private final LikeReviewService likeReviewService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    /**
     * 리뷰좋아요
     */
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


    /**
     * 리뷰 좋아요 취소
     */
    @DeleteMapping("/api/v1/like-review")
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


    /**
     * 리뷰좋아요 전체 조회
     */
    @GetMapping("/api/v1/like-review")
    public Result likeReviews() {
        List<LikeReview> likeReviewList = likeReviewService.findAll();
        List<LikeReviewDto> likeReviewDtoList = likeReviewList.stream()
                .map(lr -> new LikeReviewDto(lr.getId(), lr.getMember().getId(),
                        lr.getReview().getId(), lr.getLikeDate())).collect(Collectors.toList());
        return new Result(likeReviewDtoList.size(), likeReviewDtoList);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class LikeReviewDto {
        private Long like_review_id;
        private Long member_id;
        private Long review_id;
        private LocalDateTime date;
    }
}
