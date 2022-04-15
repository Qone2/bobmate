package com.bobmate.bobmate.api;

import com.bobmate.bobmate.config.security.JwtTokenProvider;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 회원관련
 */
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

//    @PostMapping("/api/v1/member")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setEmail(request.getEmail());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String email;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long member_id;
    }


    /**
     * 전체회원 조회
     */
    @GetMapping("/api/v1/member")
    @ApiOperation(value = "전체회원 조회")
    public Result membersV1() {
        List<Member> memberList = memberService.findAll();
        List<MemberDto> collect = memberList.stream()
                .map(m -> new MemberDto(m.getId(), m.getEmail(),
                        m.getReviews().stream().map(r -> r.getId()).collect(Collectors.toList()),
                        m.getMemberMeets().stream().map(mm -> mm.getMeet().getId()).collect(Collectors.toList()),
                        m.getLikeReviews().stream().map(lr -> lr.getReview().getId()).collect(Collectors.toList()),
                        m.getFollowers().stream().map(f -> f.getFromMember().getId()).collect(Collectors.toList()),
                        m.getFollowing().stream().map(f -> f.getToMember().getId()).collect(Collectors.toList())))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private Long member_id;
        private String email;
        private List<Long> review_ids;
        private List<Long> meet_ids;
        private List<Long> liked_review_ids;
        private List<Long> follower_ids;
        private List<Long> following_ids;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }


    /**
     * 멤버상세 조회
     */
    @GetMapping("/api/v1/member/{id}")
    @ApiOperation(value = "멤버상세 조회")
    public MemberDetailResponse memberDetailV1(@PathVariable("id") Long id) {
        Member member = memberService.findOne(id);
        return new MemberDetailResponse(member.getId(), member.getEmail(),
                member.getReviews().stream().map(r -> r.getId()).collect(Collectors.toList()),
                member.getMemberMeets().stream().map(mm -> mm.getMeet().getId()).collect(Collectors.toList()),
                member.getLikeReviews().stream().map(lr -> lr.getReview().getId()).collect(Collectors.toList()),
                member.getFollowers().stream().map(f -> f.getFromMember().getId()).collect(Collectors.toList()),
                member.getFollowing().stream().map(f -> f.getToMember().getId()).collect(Collectors.toList()),
                member.getBookmarks().stream().map(bm -> bm.getMember().getId()).collect(Collectors.toList()));
    }

    @Data
    @AllArgsConstructor
    static class MemberDetailResponse {
        private Long member_id;
        private String email;
        private List<Long> review_ids;
        private List<Long> meet_ids;
        private List<Long> liked_review_ids;
        private List<Long> follower_ids;
        private List<Long> following_ids;
        private List<Long> bookmark_ids;
    }

    /**
     * 회원가입
     */
    @PostMapping("/api/v2/join")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "회원가입")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequestV2 request) {
        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setRoles(Collections.singletonList("ROLE_USER"));
        return new CreateMemberResponse(memberService.join(member));
    }

    @Data
    static class CreateMemberRequestV2 {
        @NotEmpty
        @Email
        private String email;

        @NotEmpty
        private String password;
    }

    /**
     * 로그인
     */
    @GetMapping("/api/v2/login")
    @ApiOperation(value = "로그인")
    public LoginResponse loginV2(@RequestBody @Valid LoginRequest request) {
        Member member = memberService.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 불일치합니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("회원정보가 불일치합니다.");
        }
        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
        return new LoginResponse(member.getId(), token);
    }

    @Data
    @AllArgsConstructor
    static class LoginResponse {
        private Long member_id;
        private String token;
    }

    @Data
    static class LoginRequest {
        @NotEmpty
        @Email
        private String email;

        @NotEmpty
        private String password;
    }


    /**
     * 맴버 삭제
     */
    @DeleteMapping("/api/v1/member/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "맴버 삭제")
    public DeleteMemberResponse deleteMemberV1(@PathVariable("id") Long member_id) {
        memberService.deleteMember(member_id);
        return new DeleteMemberResponse("삭제성공");
    }

    @Data
    @AllArgsConstructor
    static class DeleteMemberResponse {
        private String message;
    }
}
