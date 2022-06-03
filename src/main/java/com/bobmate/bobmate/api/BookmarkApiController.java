package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
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
    @Operation(summary = "북마크 등록", description = "북마크를 등록합니다. schema버튼을 누르면 상세설명.<br>" +
            "북마크를 등록하는 멤버와 장소 명시.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public CreateBookmarkResponse createBookmarkV1(@RequestBody @Valid CreateBookmarkRequest request) {
        Long bookmarkId = bookmarkService.saveBookmark(request.getMember_id(), request.getPlace_id());
        return new CreateBookmarkResponse(bookmarkId);
    }

    @Data
    @AllArgsConstructor
    static class CreateBookmarkResponse {
        @Schema(description = "등록된 북마크 id")
        private Long bookmark_id;
    }

    @Data
    static class CreateBookmarkRequest {
        @NotNull
        @Schema(description = "북마크를 등록하는 멤버", required = true)
        private Long member_id;
        @NotNull
        @Schema(description = "북마크되는 장소", required = true)
        private Long place_id;
    }


    /**
     * 북마크 삭제
     */
    @DeleteMapping("/api/v1/bookmark")
    @Operation(summary = "북마크 삭제", description = "북마크를 취소(삭제)합니다.<br>" +
            "멤버와 장소를 명시하여 요청하면 삭제됩니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public DeleteBookmarkResponse deleteBookmarkV1(@RequestBody @Valid DeleteBookmarkRequest request) {
        return new DeleteBookmarkResponse(
                bookmarkService.deleteBookmark(request.getMember_id(), request.getPlace_id()));
    }

    @Data
    @AllArgsConstructor
    static class DeleteBookmarkResponse {
        @Schema(description = "삭제된 북마크 id")
        private Long bookmark_id;
    }

    @Data
    static class DeleteBookmarkRequest {
        @NotNull
        @Schema(description = "북마크를 소유한 멤버 id", required = true)
        private Long member_id;
        @NotNull
        @Schema(description = "북마크된 장소", required = true)
        private Long place_id;
    }


    /**
     * 북마크 전체 조회
     */
    @GetMapping("/api/v1/bookmark")
    @Operation(summary = "북마크 전체 조회", description = "전체 북마크정보를 조회합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
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
    @Operation(summary = "북마크 상세 조회", description = "북마크정보를 상세조회합니다. schema버튼을 누르면 상세 정보.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public BookmarkDetailResponse bookmarkDetailV1(@PathVariable Long id) {
        Bookmark findBookmark = bookmarkService.findOne(id);
        return new BookmarkDetailResponse(findBookmark.getId(), findBookmark.getMember().getId(),
                findBookmark.getPlace().getId(), findBookmark.getBookmarkedDate());
    }

    @Data
    @AllArgsConstructor
    static class BookmarkDetailResponse {
        @Schema(description = "북마크 id")
        private Long bookmark_id;
        @Schema(description = "북마크를 소유한 멤버id")
        private Long member_id;
        @Schema(description = "북마크된 장소 id")
        private Long place_id;
        @Schema(description = "북마크한 날짜")
        private LocalDateTime bookmarked_date;
    }
}
