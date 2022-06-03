package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.LikeReview;
import com.bobmate.bobmate.service.LikeReviewService;
import com.bobmate.bobmate.service.MemberService;
import com.bobmate.bobmate.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(summary = "리뷰좋아요", description = "리뷰에 좋아요를 합니다. 좋아요를 수행하는 멤버와 장소가 명시되어야합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "400 : 이미 좋아요 되어있는 경우, 삭제된 리뷰인 경우<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public LikeReviewResponse likeReviewV1(@RequestBody @Valid LikeReviewRequest request) {
        Long likeReviewId =  likeReviewService.likeReview(request.getMember_id(), request.getReview_id());
        return new LikeReviewResponse(likeReviewId);
    }

    @Data
    @AllArgsConstructor
    static class LikeReviewResponse {
        @Schema(description = "생선된 좋아요id")
        private Long like_review_id;
    }

    @Data
    static class LikeReviewRequest {
        @NotNull
        @Schema(description = "좋아요를 신청하는 멤버id", required = true)
        private Long member_id;
        @NotNull
        @Schema(description = "좋아요의 대상이 되는 리뷰", required = true)
        private Long review_id;
    }


    /**
     * 리뷰 좋아요 취소
     */
    @DeleteMapping("/api/v1/like-review")
    @Operation(summary = "리뷰 좋아요 취소", description = "리뷰에 좋아요 한것을 취소(삭제)합니다. 좋아요를 수행한 멤버와, 리뷰의 " +
            "id가 명시되어야 합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public UnlikeReviewResponse unlikeReviewV1(@RequestBody @Valid UnlikeReviewRequest request) {
        likeReviewService.unlikeReview(request.getMember_id(), request.getReview_id());
        return new UnlikeReviewResponse("done!");
    }

    @Data
    @AllArgsConstructor
    static class UnlikeReviewResponse {
        @Schema(description = "수행결과 메시지")
        private String massage;
    }

    @Data
    static class UnlikeReviewRequest {
        @NotNull
        @Schema(description = "좋아요한 멤버id")
        private Long member_id;
        @NotNull
        @Schema(description = "좋아요 되어있는 리뷰id")
        private Long review_id;
    }


    /**
     * 리뷰좋아요 전체 조회
     */
    @GetMapping("/api/v1/like-review")
    @Operation(summary = "리뷰좋아요 전체 조회", description = "리뷰에 좋아요한 정보를 전체조회합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public Result likeReviewsV1() {
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


    /**
     * 리뷰좋아요 상세 조회
     */
    @GetMapping("/api/v1/like-review/{id}")
    @Operation(summary = "리뷰좋아요 상세 조회", description = "리뷰에 좋아요한 정보를 상세조회합니다. schema버튼을 누르면 " +
            "상세정보 제공.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public LikeReviewDetailResponse likeReviewDetailV1(@PathVariable Long id) {
        LikeReview findLikeReview = likeReviewService.findOne(id);
        return new LikeReviewDetailResponse(findLikeReview.getId(), findLikeReview.getMember().getId(),
                findLikeReview.getReview().getId(), findLikeReview.getLikeDate());
    }

    @Data
    @AllArgsConstructor
    static class LikeReviewDetailResponse {
        @Schema(description = "리뷰좋아요id")
        private Long like_review_id;
        @Schema(description = "좋아요한 멤버의 id")
        private Long member_id;
        @Schema(description = "좋아요 당한 리뷰의 id")
        private Long review_id;
        @Schema(description = "좋아요 한 날짜")
        private LocalDateTime date;
    }
}
