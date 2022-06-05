package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Follow;
import com.bobmate.bobmate.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 팔로우 관련
 */
@RestController
@RequiredArgsConstructor
public class FollowApiController {

    private final FollowService followService;

    /**
     * 팔로우 하기
     */
    @PostMapping("/api/v1/follow")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "팔로우 하기", description = "from멤버와 to멤버를 명시해야 합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "400 : 이미 팔로우 되어있는 경우<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public FollowResponse followV1(@RequestBody @Valid FollowRequest request) {
        Long followId = followService.follow(request.getFromId(), request.getToId());
        return new FollowResponse(followId);
    }

    @Getter
    @AllArgsConstructor
    static class FollowResponse {
        @Schema(description = "생성된 팔로우id")
        private Long followId;
    }

    @Getter
    static class FollowRequest {
        @NotNull
        @Schema(description = "팔로우 하는 멤버id", required = true)
        private Long fromId;
        @NotNull
        @Schema(description = "팔로우 당하는 멤버id", required = true)
        private Long toId;
    }


    /**
     * 팔로우 취소
     */
    @DeleteMapping("/api/v1/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "팔로우 취소", description = "팔로우를 취소합니다. from멤버와 to멤버를 명시해야합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public UnfollowResponse unfollowV1(@RequestBody @Valid UnfollowRequest request) {
        followService.unfollow(request.getFromId(), request.getToId());
        return new UnfollowResponse("unfollowed");
    }

    @Getter
    @AllArgsConstructor
    static class UnfollowResponse {
        private String massage;
    }

    @Getter
    static class UnfollowRequest {
        @NotNull
        @Schema(description = "팔로우 하는 멤버id", required = true)
        private Long fromId;
        @NotNull
        @Schema(description = "팔로우 당하는 멤버id", required = true)
        private Long toId;
    }


    /**
     * 팔로우 전체 조회
     */
    @GetMapping("/api/v1/follow")
    @Operation(summary = "팔로우 전체 조회", description = "전체 팔로우 정보를 조회합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public Result followsV1() {
        List<Follow> followList = followService.findAll();
        List<FollowDto> followDtoList = followList.stream()
                .map(f -> new FollowDto(f.getId(), f.getFromMember().getId(), f.getToMember().getId()))
                .collect(Collectors.toList());
        return new Result(followDtoList.size(), followDtoList);
    }

    @Getter
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Getter
    @AllArgsConstructor
    static class FollowDto {
        private Long follow_id;
        private Long from_member_id;
        private Long to_member_id;
    }


    /**
     * 팔로우 상세 조회
     */
    @GetMapping("/api/v1/follow/{follow_id}")
    @Operation(summary = "팔로우 상세 조회", description = "팔로우정보를 상세조회 합니다. schema버튼을 누르면 상세정보제공.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public FollowDetailResponse followDetailV1(@PathVariable Long follow_id) {
        Follow findFollow = followService.findOne(follow_id);
        return new FollowDetailResponse(findFollow.getId(), findFollow.getFromMember().getId(),
                findFollow.getToMember().getId());
    }

    @Getter
    @AllArgsConstructor
    static class FollowDetailResponse {
        @Schema(description = "팔로우 id")
        private Long follow_id;
        @Schema(description = "팔로우 하는 멤버id")
        private Long from_member_id;
        @Schema(description = "팔로우 당하는 멤버id")
        private Long to_member_id;
    }
}
