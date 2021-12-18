package com.bobmate.bobmate.api;

import com.bobmate.bobmate.config.security.JwtTokenProvider;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/v1/member")
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
        private Long id;
    }

    @GetMapping("/api/v1/member")
    public Result membersV1() {
        List<Member> memberList = memberService.findAll();
        List<MemberDto> collect = memberList.stream()
                .map(m -> new MemberDto(m.getId(), m.getEmail()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private Long member_id;
        private String email;
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
        return new MemberDetailResponse(member.getId(), member.getEmail()
                , member.getReviews().stream().map(r -> r.getId()).collect(Collectors.toList())
                , member.getMemberMeets().stream().map(mm -> mm.getMeet().getId()).collect(Collectors.toList()));
    }

    @Data
    @AllArgsConstructor
    static class MemberDetailResponse {
        private Long member_id;
        private String email;
        private List<Long> review_ids;
        private List<Long> meet_ids;
    }

    // 회원가입
    @PostMapping("/api/v2/join")
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

    // 로그인
    @PostMapping("/api/v2/login")
    public String loginV2(@RequestBody @Valid LoginRequest request) {
        Member member = memberService.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 불일치합니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("회원정보가 불일치합니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }

    @Data
    static class LoginRequest {
        @NotEmpty
        @Email
        private String email;

        @NotEmpty
        private String password;
    }
}
