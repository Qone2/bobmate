package com.bobmate.bobmate.api;

import com.bobmate.bobmate.config.security.JwtTokenProvider;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.MemberStatus;
import com.bobmate.bobmate.dto.CreateMemberDto;
import com.bobmate.bobmate.service.MemberService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
//    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request) {
//        CreateMemberDto member = new Member();
//        member.setEmail(request.getEmail());
//        Long id = memberService.join(member);
//        return new CreateMemberResponse(id);
//    }
//
//    @Getter
//    static class CreateMemberRequest {
//        @NotEmpty
//        private String email;
//    }

    @Getter
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long member_id;
    }


    /**
     * 전체멤버 조회
     */
    @GetMapping("/api/v1/member")
    @Operation(summary = "전체멤버 조회", description = "전체 멤버정보를 조회<br><br>발생가능한 예외:<br>" +
            "400 : 내부적으로 정의된 처리 불가능의 경우 ex) 회원명 중복<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public Result membersV1() {
        List<Member> memberList = memberService.findAll();
        List<MemberDto> collect = memberList.stream()
                .map(m -> new MemberDto(m))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Getter
    @AllArgsConstructor
    static class MemberDto {
        private Long member_id;
        private String user_name;
        private String nickname;
        private List<Long> review_ids;
        private List<Long> meet_ids;
        private List<Long> liked_review_ids;
        private List<Long> follower_ids;
        private List<Long> following_ids;
        private List<Long> bookmark_ids;
        private MemberStatus member_status;

        public MemberDto(Member member) {
            this.member_id = member.getId();
            this.user_name = member.getUserName();
            this.nickname = member.getNickname();
            this.review_ids = member.getReviews()
                    .stream().map(r -> r.getId()).collect(Collectors.toList());
            this.meet_ids = member.getMemberMeets()
                    .stream().map(mm -> mm.getMeet().getId()).collect(Collectors.toList());
            this.liked_review_ids = member.getLikeReviews()
                    .stream().map(lr -> lr.getReview().getId()).collect(Collectors.toList());
            this.follower_ids = member.getFollowers()
                    .stream().map(f -> f.getFromMember().getId()).collect(Collectors.toList());
            this.following_ids = member.getFollowing()
                    .stream().map(f -> f.getToMember().getId()).collect(Collectors.toList());
            this.bookmark_ids = member.getBookmarks()
                    .stream().map(b -> b.getId()).collect(Collectors.toList());
            this.member_status = member.getMemberStatus();
        }
    }

    @Getter
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }


    /**
     * 멤버상세 조회
     */
    @GetMapping("/api/v1/member/{member_id}")
    @Operation(summary = "멤버상세 조회", description = "멤버정보 상세 조회. schema를 누르면 추가설명.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(
                            schema = @Schema(implementation = MemberDetailResponse.class)))
            })
    public MemberDetailResponse memberDetailV1(@PathVariable("member_id") Long member_id) {
        Member member = memberService.findOne(member_id);
        return new MemberDetailResponse(member);
    }

    @Getter
    @AllArgsConstructor
    static class MemberDetailResponse {
        private Long member_id;
        @Schema(description = "멤버가 설정한 아이디")
        private String user_name;
        @Schema(description = "멤버가 설정한 닉네임")
        private String nickname;
        @Schema(description = "작성한 리뷰 id리스트")
        private List<Long> review_ids;
        @Schema(description = "참가한 소모임 id리스트")
        private List<Long> meet_ids;
        @Schema(description = "좋아요한 리뷰 id리스트")
        private List<Long> liked_review_ids;
        @Schema(description = "자신을 팔로우하는 멤버 id리스트")
        private List<Long> follower_ids;
        @Schema(description = "자신이 팔로우하는 멤버 id리스트")
        private List<Long> following_ids;
        @Schema(description = "북마크(장소를 북마크)한 북마크 id리스트")
        private List<Long> bookmark_ids;
        @Schema(description = "멤버 상태 (VALID, DELETED)")
        private MemberStatus member_status;

        public MemberDetailResponse(Member member) {
            this.member_id = member.getId();
            this.user_name = member.getUserName();
            this.nickname = member.getNickname();
            this.review_ids = member.getReviews()
                    .stream().map(r -> r.getId()).collect(Collectors.toList());
            this.meet_ids = member.getMemberMeets()
                    .stream().map(mm -> mm.getMeet().getId()).collect(Collectors.toList());
            this.liked_review_ids = member.getLikeReviews()
                    .stream().map(lr -> lr.getReview().getId()).collect(Collectors.toList());
            this.follower_ids = member.getFollowers()
                    .stream().map(f -> f.getFromMember().getId()).collect(Collectors.toList());
            this.following_ids = member.getFollowing()
                    .stream().map(f -> f.getToMember().getId()).collect(Collectors.toList());
            this.bookmark_ids = member.getBookmarks()
                    .stream().map(b -> b.getId()).collect(Collectors.toList());
            this.member_status = member.getMemberStatus();
        }
    }

    /**
     * 회원가입
     */
    @PostMapping("/api/v2/join")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입", description = "**지금은 회원명이 이메일을 기준으로 되어있습니다. 추후 변경예정<br><br>" +
            "회원명 이메일 형식을 지켜줘야 허가가 나고 중복검사를 합니다. 비밀번호는 아직 따로 제한사항이 없습니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "400 : 회원명이 중복되는 경우, 이메일 형식을 지키지 않은 경우<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequestV2 request) {
        CreateMemberDto memberDto = new CreateMemberDto(request.getUser_name(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname(),
                Collections.singletonList("ROLE_USER"));
        return new CreateMemberResponse(memberService.join(memberDto));
    }

    @Getter
    static class CreateMemberRequestV2 {
        @NotNull
        @Pattern(regexp = "^[a-z\\d_\\-]{5,20}$", message = "아이디 생성 규칙을 만족하지 않습니다.")
        private String user_name;

        @NotNull
        @Pattern(regexp = "^[A-Za-z\\d_\\-!@#$%^+]{8,16}$", message = "비밀번호 생성 규칙을 만족하지 않습니다.")
        private String password;

        @NotNull
        @Pattern(regexp = "^[가-힣A-Za-z\\d]{2,12}$", message = "닉네임 생성 규칙을 만족하지 않습니다.")
        private String nickname;
    }

    /**
     * 아이디 중복확인
     */
    @GetMapping("/api/v1/member/validate-id")
    @Operation(summary = "아이디 중복 조회")
    public ResponseEntity<ValidateResponse> validateUserid(@RequestParam @NotBlank String user_id) {
        Optional<Member> optionalMember = memberService.findByUserName(user_id);
        if (optionalMember.isPresent()) {
            return ResponseEntity.ok().body(new ValidateResponse("200", "해당 아이디가 이미 존재합니다."));
        } else {
            return ResponseEntity.ok().body(new ValidateResponse("404", "해당 아이디가 아직 없습니다."));
        }
    }

    @Getter
    @AllArgsConstructor
    static class ValidateResponse {
        private String code;
        private String message;
    }

    /**
     * 로그인
     */
    @GetMapping("/api/v2/login")
    @Operation(summary = "로그인", description = "**지금은 회원명, 비밀번호가 일치할 시 토큰을 반환하지만, 그 토큰으로 따로 인증작업을 하고" +
            "있지는 않습니다.<br><br>" +
            "swagger hub내에서 GET메소드로 Request body에 담아보내는 것이 불가능하여 파라미터형식으로 바꿉니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "400 : 아이디나 비밀번호가 틀린 경우<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public LoginResponse loginV2(@ModelAttribute @Valid LoginRequest request) {
        Member member = memberService.findByUserName(request.getUser_name())
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 불일치합니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("회원정보가 불일치합니다.");
        }
        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
        return new LoginResponse(member.getId(), token);
    }

    @Getter
    @AllArgsConstructor
    static class LoginResponse {
        private Long member_id;
        private String token;
    }

    @Getter
    static class LoginRequest {
        @NotEmpty
        private String user_name;

        @NotEmpty
        private String password;
    }


    /**
     * 맴버 삭제 v1
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "맴버 삭제")
    public DeleteMemberResponse deleteMemberV1(@PathVariable("id") Long member_id) {
        memberService.deleteMember(member_id);
        return new DeleteMemberResponse("삭제성공");
    }

    @Getter
    @AllArgsConstructor
    static class DeleteMemberResponse {
        private String message;
    }


    /**
     * 맴버 삭제 v2
     */
    @DeleteMapping("/api/v2/member/{member_id}")
    @Operation(summary = "맴버 삭제", description = "멤버삭제는 물리적 삭제가 아닌 논리적 삭제로 진행되며, " +
            "member status 항목이 VALID에서 DELETED로 바뀝니다.<br><br>" + "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public DeleteMemberResponseV2 deleteMemberV2(@PathVariable("member_id") Long member_id) {
        return new DeleteMemberResponseV2(memberService.deleteMember(member_id));
    }

    @Getter
    @AllArgsConstructor
    static class DeleteMemberResponseV2 {
        private Long member_id;
    }
}
