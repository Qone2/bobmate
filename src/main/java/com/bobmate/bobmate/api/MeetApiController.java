package com.bobmate.bobmate.api;

import com.bobmate.bobmate.service.MeetService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class MeetApiController {

    private final MeetService meetService;

    @PostMapping("/api/v1/meet")
    public CreateMeetResponse createMeet(@RequestBody @Valid CreateMeetRequest request) {
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
    public CreateMemberMeetResponse addMember(@PathVariable("meet_id") Long meet_id, @RequestBody @Valid CreateMemberMeetRequest request) {
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


}
