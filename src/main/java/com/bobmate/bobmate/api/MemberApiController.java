package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/member")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @GetMapping("/api/v1/member")
    public Result membersV1() {
        List<Member> memberList = memberService.findAll();
        List<MemberDto> collect = memberList.stream()
                .map(m -> new MemberDto(m.getId(), m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private Long member_id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @GetMapping("/api/v1/member/{id}")
    public MemberDetailResponse memberDetailV1(@PathVariable("id") Long id) {
        Member member = memberService.findOne(id);
        return new MemberDetailResponse(member.getId(), member.getName()
                , member.getReviews().stream().map(r -> r.getId()).collect(Collectors.toList())
                , member.getMemberMeets().stream().map(mm -> mm.getMeet().getId()).collect(Collectors.toList()));
    }

    @Data
    @AllArgsConstructor
    static class MemberDetailResponse {
        private Long member_id;
        private String name;
        private List<Long> review_ids;
        private List<Long> meet_ids;
    }
}
