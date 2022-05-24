package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.service.BookmarkService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "북마크 등록")
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
    @ApiOperation(value = "북마크 삭제")
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


    /**
     * 북마크 전체 조회
     */
    @GetMapping("/api/v1/bookmark")
    @ApiOperation("북마크 전체 조회")
    public Result bookmarksV1() {
        List<Bookmark> bookmarkList = bookmarkService.findAll();
        List<BookmarkDto> bookmarkDtoList = bookmarkList.stream()
                .map(bm -> new BookmarkDto(bm.getId(),
                        bm.getMember().getId(),
                        bm.getPlace().getId(),
                        bm.getBookmarkedDate())).collect(Collectors.toList());
        return new Result(bookmarkDtoList.size(), bookmarkDtoList);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class BookmarkDto {
        private Long bookmark_id;
        private Long member_id;
        private Long place_id;
        private LocalDateTime bookmarked_date;
    }


    /**
     * 북마크 상세 조회
     */
    @GetMapping("/api/v1/bookmark/{id}")
    @ApiOperation(value = "북마크 상세 조회")
    public BookmarkDetailResponse bookmarkDetailV1(@PathVariable Long id) {
        Bookmark findBookmark = bookmarkService.findOne(id);
        return new BookmarkDetailResponse(findBookmark.getId(), findBookmark.getMember().getId(),
                findBookmark.getPlace().getId(), findBookmark.getBookmarkedDate());
    }

    @Data
    @AllArgsConstructor
    static class BookmarkDetailResponse {
        private Long bookmark_id;
        private Long member_id;
        private Long place_id;
        private LocalDateTime bookmarked_date;
    }
}
