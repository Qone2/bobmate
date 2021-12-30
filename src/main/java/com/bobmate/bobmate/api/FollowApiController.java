package com.bobmate.bobmate.api;

import com.bobmate.bobmate.service.FollowService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class FollowApiController {

    private final FollowService followService;

    @PostMapping("/api/v1/follow")
    @ResponseStatus(HttpStatus.CREATED)
    public FollowResponse followV1(@RequestBody @Valid FollowRequest request) {
        Long followId = followService.follow(request.getFromId(), request.getToId());
        return new FollowResponse(followId);
    }

    @Data
    @AllArgsConstructor
    static class FollowResponse {
        private Long followId;
    }

    @Data
    static class FollowRequest {
        @NotNull
        private Long fromId;
        @NotNull
        private Long toId;
    }


    @DeleteMapping("/api/v1/unfollow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UnfollowResponse unfollowV1(@RequestBody @Valid UnfollowRequest request) {
        followService.unfollow(request.getFromId(), request.getToId());
        return new UnfollowResponse("unfollowed");
    }

    @Data
    @AllArgsConstructor
    static class UnfollowResponse {
        private String massage;
    }

    @Data
    static class UnfollowRequest {
        @NotNull
        private Long fromId;
        @NotNull
        private Long toId;
    }
}
