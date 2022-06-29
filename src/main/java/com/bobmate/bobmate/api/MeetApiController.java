package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Meet;
import com.bobmate.bobmate.service.MeetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 모임 관련
 */
@RestController
@RequiredArgsConstructor
public class MeetApiController {

    private final MeetService meetService;


    /**
     * 모임 생성
     */
    @PostMapping("/api/v1/meet")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "소모임 생성", description = "소모임을 생성합니다. 소모임을 생성하는 방장멤버, 장소, 카카오톡 오픈체팅 링크가 " +
            "필요합니다. schema버튼을 누르면 상세정보 제공.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public CreateMeetResponse createMeetV1(@RequestBody @Valid CreateMeetRequest request) {
        return new CreateMeetResponse(meetService.saveMeet(request.getMember_id(), request.getPlace_id(),
                request.getName(), request.getLink()));
    }

    @Getter
    @AllArgsConstructor
    static class CreateMeetResponse {
        @Schema(description = "생성된 소모임의 id")
        private Long meet_id;
    }

    @Getter
    static class CreateMeetRequest {
        @NotNull
        @Schema(description = "생성하는 소모임의 방장이 될 멤버id", required = true)
        private Long member_id;
        @NotNull
        @Schema(description = "생성하는 소모임의 장소", required = true)
        private Long place_id;
        @NotEmpty
        @Schema(description = "생성하는 소모임의 이름", required = true)
        private String name;
        @NotEmpty
        @URL
        @Schema(description = "소모임의 카카오톡 오픈채팅 링크", required = true)
        private String link;
    }


    /**
     * 모임에 맴버 추가
     */
    @PostMapping("/api/v1/meet/member/{meet_id}")
    @Operation(summary = "소모임에 맴버 추가", description = "소모임에 멤버를 추가합니다. path variable에 소모임id가 명시되어야 하고," +
            "json형식으로 추가될 멤버의 id가 명시되어야 합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "409 : 멤버가 이미 소모임에 참여되어있는 경우<br>" +
            "500 : 내부 서버 에러")
    public CreateMemberMeetResponse addMemberMeetV1(@PathVariable("meet_id") Long meet_id,
                                                    @RequestBody @Valid CreateMemberMeetRequest request) {
        meetService.addMember(request.getMember_id(), meet_id);
        return new CreateMemberMeetResponse("success");
    }

    @Getter
    @AllArgsConstructor
    static class CreateMemberMeetResponse {
        private String message;
    }

    @Getter
    static class CreateMemberMeetRequest {
        @NotNull
        @Schema(description = "추가할 멤버id")
        private Long member_id;
    }


    /**
     * 전체 모임 조회
     */
    @GetMapping("/api/v1/meet")
    @Operation(summary = "전체 소모임 조회", description = "전체 소모임정보를 조회합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public Result meetsV1() {
        List<MeetDto> collect = meetService.findAll()
                .stream().map(m -> new MeetDto(m))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Getter
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Getter
    @AllArgsConstructor
    static class MeetDto {
        private Long meet_id;
        private Long head_member_id;
        private Long place_id;
        private List<Long> member_ids;
        private String name;
        private String link;
        private int member_count;
        private LocalDateTime created_date;

        public MeetDto(Meet meet) {
            this.meet_id = meet.getId();
            this.head_member_id = meet.getHeadMember().getId();
            this.place_id = meet.getPlace().getId();
            this.member_ids = meet.getMemberMeets().stream().map(mm -> mm.getMember().getId())
                    .collect(Collectors.toList());
            this.name = meet.getName();
            this.link = meet.getLink();
            this.member_count = meet.getMemberCount();
            this.created_date = meet.getCreatedDate();
        }
    }


    /**
     * 모임 상세 조회
     */
    @GetMapping("/api/v1/meet/{meet_id}")
    @Operation(summary = "소모임 상세 조회", description = "소모임을 상세조회 합니다. schema버튼을 누르면 상세정보제공.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public MeetDetailResponse meetDetailV1(@PathVariable("meet_id") Long meet_id) {
        Meet meet = meetService.findOne(meet_id);
        return new MeetDetailResponse(meet);
    }

    @Getter
    @AllArgsConstructor
    static class MeetDetailResponse {
        @Schema(description = "소모임 id")
        private Long meet_id;
        @Schema(description = "소모임의 방장멤버 id")
        private Long head_member_id;
        @Schema(description = "소모임대상 장소")
        private Long place_id;
        @Schema(description = "소모임에 속한 멤버id 리스트")
        private List<Long> member_ids;
        @Schema(description = "소모임 이름")
        private String name;
        @Schema(description = "소모임 카카오톡 오픈채팅 링크")
        private String link;
        @Schema(description = "소모임 멤버수")
        private int member_count;
        @Schema(description = "소모임이 생성된 날짜")
        private LocalDateTime created_date;

        public MeetDetailResponse(Meet meet) {
            this.meet_id = meet.getId();
            this.head_member_id = meet.getHeadMember().getId();
            this.place_id = meet.getPlace().getId();
            this.member_ids = meet.getMemberMeets().stream().map(mm -> mm.getMember().getId())
                    .collect(Collectors.toList());
            this.name = meet.getName();
            this.link = meet.getLink();
            this.member_count = meet.getMemberCount();
            this.created_date = meet.getCreatedDate();
        }
    }


    /**
     * 모임에 맴버 삭제
     */
    @DeleteMapping("/api/v1/meet/member/{meet_id}")
    @Operation(summary = "소모임에 맴버 삭제", description = "소모임의 멤버를 탈퇴시킵니다. path variable에 소모임 id가 명시되어있어야 " +
            "하고, json에 탈퇴할 멤버 id가 명시되어있어야 합니다.<br>" +
            "방장멤버는 탈퇴가 불가능 합니다.<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "422 : 탈퇴될 멤버가 방장멤버인 경우<br>" +
            "500 : 내부 서버 에러")
    public DeleteMemberMeetResponse deleteMemberMeetV1(@PathVariable Long meet_id,
                                                       @RequestBody @Valid DeleteMemberMeetRequest request) {
        meetService.deleteMember(request.member_id, meet_id);
        return new DeleteMemberMeetResponse("success");
    }

    @Getter
    @AllArgsConstructor
    static class DeleteMemberMeetResponse {
        private String message;
    }

    @Getter
    static class DeleteMemberMeetRequest {
        @NotNull
        @Schema(description = "탈퇴시킬 멤버id", required = true)
        private Long member_id;
    }

    /**
     * 모임 삭제
     */
    @DeleteMapping("/api/v1/meet/{meet_id}")
    @Operation(summary = "소모임 삭제", description = "소모임을 삭제합니다. 지금은 물리삭제로 진행되지만 논리삭제로 변경될 여지가 있습니다." +
            "<br><br>" +
            "발생가능한 예외:<br>" +
            "404 : 요청한 자원을 찾을 수 없는 경우<br>" +
            "500 : 내부 서버 에러")
    public DeleteMeetResponse deleteMeetV1(@PathVariable Long meet_id) {
        return new DeleteMeetResponse(meetService.deleteMeet(meet_id));
    }

    @Getter
    @AllArgsConstructor
    static class DeleteMeetResponse {
        @Schema(description = "삭제된 소모임 id")
        private Long meet_id;
    }
}
