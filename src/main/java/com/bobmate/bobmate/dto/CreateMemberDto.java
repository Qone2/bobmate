package com.bobmate.bobmate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateMemberDto {
    private String userName;
    private String password;
    private String nickname;
    private List<String> roles;
}
