package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Tag;
import com.bobmate.bobmate.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Operation(summary = "태그 등록", description = "새로운 태그를 등록합니다. schema버튼을 누르면 상세설명.<br>" +
            "**주의 : 이 요청은 북마크에 태그를 등록하는 것이 아닌 태그 자체를 새로 생성하는 요청입니다. " +
            "예) \"깔끔한\"이라는 태그카테고리를 새로 생성<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "409 : 이미 같은 이름으로 생성된 태그가 존재하는 경우<br>" +
            "500 : 내부 서버 에러")
    public CreateTagResponse createTagV1(@RequestBody @Valid CreateTagRequest request) {
        Long tagId = tagService.saveTag(request.getName());
        return new CreateTagResponse(tagId);
    }

    @Getter
    @AllArgsConstructor
    static class CreateTagResponse {
        @Schema(description = "생성된 태그id")
        private Long tag_id;
    }

    @Getter
    static class CreateTagRequest {
        @NotEmpty
        @Schema(description = "태그 명칭", required = true)
        private String name;
    }


    /**
     * 모든 태그 조회
     */
    @GetMapping("/api/v1/tag")
    @Operation(summary = "모든 태그 조회", description = "모든 종류의 태그를 조회합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public Result tagsV1() {
        List<Tag> tags = tagService.findAll();
        List<TagDto> tagDtoList = tags.stream()
                .map(t -> new TagDto(t.getId(), t.getName())).collect(Collectors.toList());
        return new Result(tagDtoList.size(), tagDtoList);
    }

    @Getter
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Getter
    @AllArgsConstructor
    static class TagDto {
        private Long tag_id;
        private String name;
    }

    /**
     * 태그 삭제
     */
    @DeleteMapping("/api/v1/tag/{tag_id}")
    @Operation(summary = "태그 삭제", description = "태그를 삭제합니다.<br>" +
            "**이 요청은 북마크에 태그를 취소하는것이 아닌 태그 자체를 삭제하는 요청입니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public DeleteTagResponse deleteTagV1(@PathVariable Long tag_id) {
        tagService.deleteTag(tag_id);
        return new DeleteTagResponse(tag_id, "성공적으로 삭제되었습니다.");
    }

    @Getter
    @AllArgsConstructor
    static class DeleteTagResponse {
        @Schema(description = "삭제된 태그id")
        private Long tag_id;
        private String message;
    }


    /**
     * 태그 상세 조회
     */
    @GetMapping("/api/v1/tag/{tag_id}")
    @Operation(summary = "태그 상세 조회", description = "태그의 상세정보를 조회합니다. schema버튼은 누르면 상세정보제공.<br>" +
            "아직 상세정보라고 해봐야 특별한 내용은 없습니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public TagDetailResponse tagDetailV1(@PathVariable Long tag_id) {
        Tag findTag = tagService.findOne(tag_id);
        return new TagDetailResponse(findTag.getId(), findTag.getName());
    }

    @Getter
    @AllArgsConstructor
    static class TagDetailResponse {
        @Schema(description = "태그 id")
        private Long tag_id;
        @Schema(description = "태그의 명칭")
        private String name;
    }


    /**
     * 모든 태그 태그된 횟수순으로 내림차순 조회
     */
    @GetMapping("/api/v1/tag/descending")
    @Operation(summary = "모든 태그 태그된 횟수순으로 내림차순 조회", description = "모든 태그정보를 조회하는데, 태그가 가장 많이 된" +
            "횟수 순으로 조회됩니다. 태그를 추천하는 목록을 제시하는 용도로 사용됩니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public Result tagsDescendingV1() {
        List<Map.Entry<Long, Integer>> entryList = tagService.findAllByTaggedCount();
        List<TagDescendingDto> tagDescendingDtoList = new ArrayList<>();
        entryList.stream().map(e -> tagDescendingDtoList.add(new TagDescendingDto(e.getKey(),
                tagService.findOne(e.getKey()).getName(), e.getValue())));
        return new Result(tagDescendingDtoList.size(), tagDescendingDtoList);
    }

    @Getter
    @AllArgsConstructor
    static class TagDescendingDto {
        private Long tag_id;
        private String tag_name;
        private Integer tagged_count;
    }


    /**
     * 특정 맴버의 모든 태그 태그된 횟수순으로 내림차순 조회
     */
    @GetMapping("/api/v1/tag/descending/{member_id}")
    @Operation(summary = "특정 맴버의 모든 태그 태그된 횟수순으로 내림차순 조회", description = "특정 맴버가 사용한 모든 태그를 조회하는데," +
            "그 맴버가 가장 많이 사용한 태그 순으로 정렬되어 보여집니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public Result tagsByMemberDescendingV1(@PathVariable Long member_id) {
        List<Map.Entry<Long, Integer>> entryList = tagService.findAllByMemberAndTaggedCount(member_id);
        List<TagDescendingDto> tagDescendingDtoList = new ArrayList<>();
        entryList.stream().map(e -> tagDescendingDtoList.add(new TagDescendingDto(e.getKey(),
                tagService.findOne(e.getKey()).getName(), e.getValue())));
        return new Result(tagDescendingDtoList.size(), tagDescendingDtoList);
    }
}
