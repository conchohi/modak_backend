package com.modak.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserDto {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String profileImage;
    private String sns;
    private String role;
}
