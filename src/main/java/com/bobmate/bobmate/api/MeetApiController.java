package com.bobmate.bobmate.api;

import com.bobmate.bobmate.domain.Meet;
import com.bobmate.bobmate.service.MeetService;
import lombok.AllArgsConstructor;
import lombok.Data;
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

@RestController
@RequiredArgsConstructor
public class MeetApiController {

    private final MeetService meetService;


    @PostMapping("/api/v1/meet")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateMeetResponse createMeetV1(@RequestBody @Valid CreateMeetRequest request) {
        return new CreateMeetResponse(meetService.saveMeet(request.getMember_id(), request.getPlace_id(),
                request.getName(), request.getLink()));
    }

    @Data
    @AllArgsConstructor
    static class CreateMeetResponse {
        private Long meet_id;
    }

    @Data
    static class CreateMeetRequest {
        @NotNull
        private Long member_id;
        @NotNull
        private Long place_id;
        @NotEmpty
        private String name;
        @NotEmpty
        @URL
        private String link;
    }


    @PostMapping("/api/v1/meet/{meet_id}")
    public CreateMemberMeetResponse addMemberV1(@PathVariable("meet_id") Long meet_id, @RequestBody @Valid CreateMemberMeetRequest request) {
        return new CreateMemberMeetResponse(meetService.addMember(request.getMember_id(), meet_id));
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberMeetResponse {
        private Long member_meet_id;
    }

    @Data
    static class CreateMemberMeetRequest {
        @NotNull
        private Long member_id;
    }


    @GetMapping("/api/v1/meet")
    public Result meets() {
        List<MeetDto> collect = meetService.findAll()
                .stream().map(m -> new MeetDto(m.getId(), m.getHeadMember().getId(),
                        m.getPlace().getId(), m.getName(), m.getMemberCount()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MeetDto {
        private Long meet_id;
        private Long head_member_id;
        private Long place_id;
        private String name;
        private int member_count;
    }


    @GetMapping("/api/v1/meet/{id}")
    public MeetDetailResponse meetDetail(@PathVariable("id") Long id) {
        Meet meet = meetService.findOne(id);
        return new MeetDetailResponse(meet.getId(), meet.getHeadMember().getId(), meet.getPlace().getId(),
                meet.getMemberMeets().stream().map(mm -> mm.getMember().getId()).collect(Collectors.toList()),
                meet.getName(), meet.getLink(), meet.getMemberCount(), meet.getCreatedDate());
    }

    @Data
    @AllArgsConstructor
    static class MeetDetailResponse {
        private Long meet_id;
        private Long head_member_id;
        private Long place_id;
        private List<Long> member_ids;
        private String name;
        private String link;
        private int member_count;
        private LocalDateTime created_date;
    }
}
