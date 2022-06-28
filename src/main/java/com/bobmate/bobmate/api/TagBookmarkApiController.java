package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Bookmark;
import com.bobmate.bobmate.service.TagBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 북마크 태그 관련
 */
@RestController
@RequiredArgsConstructor
public class TagBookmarkApiController {

    private final TagBookmarkService tagBookmarkService;

    /**
     * 북마크 태그 설정
     */
    @PostMapping("/api/v1/tagbookmark")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "북마크 태그 설정", description = "북마크에 태그를 등록합니다.<br>" +
            "멤버id는 검증용으로 북마크의 소유가 일치하는지 확인합니다. 필요없는 부분이라고 판단되면 알려주시기 바랍니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "409 : 북마크에 태그가 이미 등록되어있는 경우<br>" +
            "422 : 멤버가 북마크의 소유자가 아닌경우<br>" +
            "500 : 내부 서버 에러")
    public CreateTagBookmarkResponse createTagBookmarkV1(@RequestBody @Valid CreateTagBookmarkRequest request) {
        tagBookmarkService.saveTagBookmark(request.getTag_id(), request.getBookmark_id(), request.getMember_id());
        return new CreateTagBookmarkResponse("success");
    }

    @Getter
    @AllArgsConstructor
    static class CreateTagBookmarkResponse {
        private String message;
    }

    @Getter
    static class CreateTagBookmarkRequest {
        @NotNull
        @Schema(description = "검증용 멤버id(북마크의 소유자)", required = true)
        private Long member_id;
        @NotNull
        @Schema(description = "태그될 북마크id", required = true)
        private Long bookmark_id;
        @NotNull
        @Schema(description = "태그할 태그id", required = true)
        private Long tag_id;
    }


    /**
     * 해당 태그(들)을 설정한 북마크 전체 조회
     */
    @GetMapping("/api/v1/tagbookmark")
    @Operation(summary = "해당 태그(들)을 설정한 북마크 전체 조회", description = "태그id들을 리스트로 만들어 요청을 보내면" +
            "해당 태그에 해당하는 북마크id 리스트를 리턴합니다. and연산으로 처리됩니다. 북마크 소유주 멤버id가 명시되어야 합니다. <br>" +
            "태그id 리스트는 파라미터에 \",\"로 구분하여 보내주세요. ex)tag_id_list=24,25 <br>" +
            "비어있는 리스트를 보내면 해당 멤버가 북마크한 모든 북마크가 보여지고, 요청하는 태그에 해당하는 북마크가 없으면 빈 리스트를 " +
            "리턴합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public GetTagBookmarkResponse taggedBookmarksV1(@ModelAttribute @Valid GetTagBookmarkRequest request) {
        List<Bookmark> taggedBookmark = tagBookmarkService.findTaggedBookmark(
                request.getMember_id(), request.getTag_id_list());

        return new GetTagBookmarkResponse(request.getMember_id(), request.getTag_id_list(),
                taggedBookmark.stream().map(tb -> tb.getId()).collect(Collectors.toList()));
    }

    @Getter
    @AllArgsConstructor
    static class GetTagBookmarkResponse {
        @Schema(description = "요청할때 명시한 멤버id")
        private Long member_id;
        @Schema(description = "요청할때 사용한 태그 id 리스트")
        private List<Long> tag_ids;
        @Schema(description = "요청결과로 나오는 북마크id 리스트")
        private List<Long> tagged_bookmark_id_list;
    }

    @Getter
    @Setter
    static class GetTagBookmarkRequest {
        @NotNull
        @Schema(description = "불러올 북마크의 소유주 멤버 id", required = true)
        private Long member_id;
        @NotNull
        @Schema(description = "적용할 태그 id 리스트", required = true)
        private List<Long> tag_id_list;
    }


    /**
     * 북마크 태그 설정 취소
     */
    @DeleteMapping("/api/v1/tagbookmark")
    @Operation(summary = "북마크 태그 설정 취소", description = "북마크에 적용된 태그를 취소 합니다.<br>" +
            "요청하는데 필요한 멤버id는 북마크의 소유주가 맞는지 검증하는 용도 입니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "422 : 요청한 멤버가 북마크의 소유주가 아닌경우<br>" +
            "500 : 내부 서버 에러")
    public DeleteTagBookmarkResponse deleteTagBookmarkV1(@RequestBody @Valid DeleteTagBookmarkRequest request) {
        tagBookmarkService.deleteTagBookmark(request.getTag_id(), request.getBookmark_id(), request.getMember_id());
        return new DeleteTagBookmarkResponse("success");
    }

    @Getter
    @AllArgsConstructor
    static class DeleteTagBookmarkResponse {
        private String massage;
    }

    @Getter
    static class DeleteTagBookmarkRequest {
        @NotNull
        @Schema(description = "북마크 검증용 멤버id", required = true)
        private Long member_id;
        @NotNull
        @Schema(description = "태그 해제될 북마크 id", required = true)
        private Long bookmark_id;
        @NotNull
        @Schema(description = "해제할 태그id", required = true)
        private Long tag_id;
    }
}
