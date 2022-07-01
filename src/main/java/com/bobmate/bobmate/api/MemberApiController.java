package com.bobmate.bobmate.api;

import com.bobmate.bobmate.config.security.JwtTokenProvider;
import com.bobmate.bobmate.domain.Member;
import com.bobmate.bobmate.domain.MemberStatus;
import com.bobmate.bobmate.dto.CreateMemberDto;
import com.bobmate.bobmate.exception.WrongIdPasswordException;
import com.bobmate.bobmate.service.MemberService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 회원관련
 */
@RestController
@RequiredArgsConstructor
@Validated
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
        private String user_id;
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
            this.user_id = member.getUserId();
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
        private String user_id;
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
            this.user_id = member.getUserId();
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
    @Operation(summary = "회원가입", description = "아이디, 닉네임에 대해서 중복검사가 있습니다.<br>" +
            "schema의 아이디, 비밀번호, 닉네임 규칙을 만족해야 합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "400 : 아이디, 비밀번호, 닉네임 형식이 지켜지지 않은 경우<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "409 : 아이디나 닉네임이 중복되는 경우<br>" +
            "500 : 내부 서버 에러")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequestV2 request) {
        CreateMemberDto memberDto = new CreateMemberDto(request.getUser_id(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname(),
                Collections.singletonList("ROLE_USER"));
        return new CreateMemberResponse(memberService.join(memberDto));
    }

    @Getter
    static class CreateMemberRequestV2 {
        @NotNull
        @Pattern(regexp = "^[a-z0-9_\\-]{5,20}$", message = "아이디 생성 규칙을 만족하지 않습니다.")
        private String user_id;

        @NotNull
        @Pattern(regexp = "^[A-Za-z0-9_\\-!@#$%^+]{8,16}$", message = "비밀번호 생성 규칙을 만족하지 않습니다.")
        private String password;

        @NotNull
        @Pattern(regexp = "^[가-힣A-Za-z0-9]{2,12}$", message = "닉네임 생성 규칙을 만족하지 않습니다.")
        private String nickname;
    }

    /**
     * 아이디 중복확인
     */
    @GetMapping("/api/v1/member/validate-id")
    @Operation(summary = "아이디 중복 조회", description = "아이디가 중복되는지 검사합니다. 중복여부 관계없이 status code는 200이고, " +
            "중복인 경우 리턴json의 code값이 \"409\", 중복이 아닌경우 \"404\"입니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "400 : query parameter 변수가 전달되지 않은 경우, 형식이 지켜지지 않은 경우<br>" +
            "500 : 내부 서버 에러")
    public ResponseEntity<ValidateResponse> validateUserid(
            @RequestParam @Pattern(regexp = "^[a-z0-9_\\-]{5,20}$", message = "아이디 생성 규칙을 만족하지 않습니다.")
            @Parameter(description = "규칙 : ^[a-z0-9_\\-]{5,20}$")
                    String user_id) {
        Optional<Member> optionalMember = memberService.findOneByUserId(user_id);
        if (optionalMember.isPresent()) {
            return ResponseEntity.ok().body(new ValidateResponse("409", "해당 아이디가 이미 존재합니다."));
        } else {
            return ResponseEntity.ok().body(new ValidateResponse("404", "해당 아이디가 아직 없습니다."));
        }
    }

    @Getter
    @AllArgsConstructor
    static class ValidateResponse {
        @Schema(description = "중복될 경우 \"409\", 중복되지 않으면 \"404\"")
        private String code;
        private String message;
    }

    /**
     * 닉네임 중복확인
     */
    @GetMapping("/api/v1/member/validate-nickname")
    @Operation(summary = "닉네임 중복 조회", description = "닉네임이 중복되는지 검사합니다. 중복여부 관계없이 status code는 200이고, " +
            "중복인 경우 리턴json의 code값이 \"409\", 중복이 아닌경우 \"404\"입니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "400 : query parameter 변수가 전달되지 않은 경우, 형식이 지켜지지 않은 경우<br>" +
            "500 : 내부 서버 에러")
    public ResponseEntity<ValidateResponse> validateNickname(
            @RequestParam @Pattern(regexp = "^[가-힣A-Za-z0-9]{2,12}$", message = "닉네임 생성 규칙을 만족하지 않습니다.")
            @Parameter(description = "규칙 : ^[가-힣A-Za-z0-9]{2,12}$")
                    String nickname) {
        Optional<Member> optionalMember = memberService.findOneByNickname(nickname);
        if (optionalMember.isPresent()) {
            return ResponseEntity.ok().body(new ValidateResponse("409", "해당 닉네임이 이미 존해합니다."));
        } else {
            return ResponseEntity.ok().body(new ValidateResponse("404", "해당 닉네임이 아직 없습니다."));
        }
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
        Member member = memberService.findOneByUserId(request.getUser_id())
                .orElseThrow(() -> new WrongIdPasswordException("회원정보가 불일치합니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new WrongIdPasswordException("회원정보가 불일치합니다.");
        }
        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
        return new LoginResponse(member.getId(), token);
    }

    @Getter
    @AllArgsConstructor
    static class LoginResponse {
        @Schema(description = "해당 멤버 id")
        private Long member_id;
        @Schema(description = "JWT 토큰")
        private String token;
    }

    @Getter
    @Setter
    static class LoginRequest {
        @NotEmpty
        private String user_id;

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
            "422 : 이미 삭제된 멤버인 경우<br>" +
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
