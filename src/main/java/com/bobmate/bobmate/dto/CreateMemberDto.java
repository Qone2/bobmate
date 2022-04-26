package com.bobmate.bobmate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateMemberDto {
    private String email;
    private String password;
    private List<String> roles;
}
