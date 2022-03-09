package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Tag;
import com.bobmate.bobmate.service.TagService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 태그 관련
 */
@RestController
@RequiredArgsConstructor
public class TagApiController {

    private final TagService tagService;

    /**
     * 태그 등록
     */
    @PostMapping("/api/v1/tag")
    public CreateTagResponse createTagV1(@RequestBody @Valid CreateTagRequest request) {
        Long tagId = tagService.saveTag(request.getName());
        return new CreateTagResponse(tagId);
    }

    @Data
    @AllArgsConstructor
    static class CreateTagResponse {
        private Long tag_id;
    }

    @Data
    static class CreateTagRequest {
        @NotEmpty
        private String name;
    }


    /**
     * 모든 태그 조회
     */
    @GetMapping("/api/v1/tag")
    public Result tagsV1() {
        List<Tag> tags = tagService.findAll();
        return new Result(tags.size(), tags);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    static class tagDto {
        private Long tag_id;
        private String name;
    }

    /**
     * 태그 삭제
     */
    @DeleteMapping("/api/v1/tag/{tag_id}")
    public DeleteTagResponse deleteTagV1(@PathVariable Long tag_id) {
        tagService.deleteTag(tag_id);
        return new DeleteTagResponse(tag_id, "성공적으로 삭제되었습니다.");
    }

    @Data
    @AllArgsConstructor
    static class DeleteTagResponse {
        private Long tag_id;
        private String message;
    }
}
