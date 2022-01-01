package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.service.TagBookmarkService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TagBookmarkApiController {

    private final TagBookmarkService tagBookmarkService;

    @PostMapping("/api/v1/tagbookmark")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTagBookmarkResponse createTagBookmarkV1(@RequestBody @Valid CreateTagBookmarkRequest request) {
        Long tagBookmarkId = tagBookmarkService.saveTagBookmark(request.getTag_id(), request.getBookmark_id(), request.getMember_id());
        return new CreateTagBookmarkResponse(tagBookmarkId);
    }

    @Data
    @AllArgsConstructor
    static class CreateTagBookmarkResponse {
        private Long tag_bookmark_id;
    }

    @Data
    static class CreateTagBookmarkRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private Long bookmark_id;
        @NotNull
        private Long tag_id;
    }


    @GetMapping("/api/v1/tagbookmark")
    public GetTagBookmarkResponse taggedBookmarksV1(@RequestBody @Valid GetTagBookmarkRequest request) {
        List<Bookmark> taggedBookmark = tagBookmarkService.findTaggedBookmark(request.getMember_id(), request.getTag_id_list());

        return new GetTagBookmarkResponse(request.getMember_id(), request.getTag_id_list(),
                taggedBookmark.stream().map(tb -> tb.getId()).collect(Collectors.toList()));
    }

    @Data
    @AllArgsConstructor
    static class GetTagBookmarkResponse {
        private Long member_id;
        private List<Long> tag_ids;
        private List<Long> tagged_bookmark_id_list;
    }

    @Data
    static class GetTagBookmarkRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private List<Long> tag_id_list;
    }


    @DeleteMapping("/api/v1/tagbookmark")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public DeleteTagBookmarkResponse deleteTagBookmark(@RequestBody @Valid DeleteTagBookmarkRequest request) {
        tagBookmarkService.deleteTagBookmark(request.getTag_id(), request.getBookmark_id(), request.getMember_id());
        return new DeleteTagBookmarkResponse("태그 취소됨");
    }

    @Data
    @AllArgsConstructor
    static class DeleteTagBookmarkResponse {
        private String massage;
    }

    @Data
    static class DeleteTagBookmarkRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private Long bookmark_id;
        @NotNull
        private Long tag_id;
    }
}
