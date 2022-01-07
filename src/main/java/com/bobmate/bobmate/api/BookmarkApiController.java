package com.bobmate.bobmate.api;

import com.bobmate.bobmate.service.BookmarkService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 북마크 관련
 */
@RestController
@RequiredArgsConstructor
public class BookmarkApiController {

    private final BookmarkService bookmarkService;

    /**
     * 북마크 등록
     */
    @PostMapping("/api/v1/bookmark")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateBookmarkResponse createBookmarkV1(@RequestBody @Valid CreateBookmarkRequest request) {
        Long bookmarkId = bookmarkService.saveBookmark(request.getMember_id(), request.getPlace_id());
        return new CreateBookmarkResponse(bookmarkId);
    }

    @Data
    @AllArgsConstructor
    static class CreateBookmarkResponse {
        private Long bookmark_id;
    }

    @Data
    static class CreateBookmarkRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private Long place_id;
    }


    /**
     * 북마크 삭제
     */
    @DeleteMapping("/api/v1/bookmark")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DeleteBookmarkResponse deleteBookmarkV1(@RequestBody @Valid DeleteBookmarkRequest request) {
        bookmarkService.deleteBookmark(request.getMember_id(), request.getPlace_id());
        return new DeleteBookmarkResponse("북마크 삭제됨");
    }

    @Data
    @AllArgsConstructor
    static class DeleteBookmarkResponse {
        private String massage;
    }

    @Data
    static class DeleteBookmarkRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private Long place_id;
    }

}
