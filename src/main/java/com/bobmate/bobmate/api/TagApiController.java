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
import java.util.stream.Collectors;

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
        List<TagDto> tagDtoList = tags.stream()
                .map(t -> new TagDto(t.getId(), t.getName())).collect(Collectors.toList());
        return new Result(tagDtoList.size(), tagDtoList);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class TagDto {
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


    /**
     * 태그 상세 조회
     */
    @GetMapping("/api/v1/tag/{id}")
    public TagDetailResponse tagDetail(@PathVariable Long id) {
        Tag findTag = tagService.findOne(id);
        return new TagDetailResponse(findTag.getId(), findTag.getName());
    }

    @Data
    @AllArgsConstructor
    static class TagDetailResponse{
        private Long tag_id;
        private String name;
    }
}
