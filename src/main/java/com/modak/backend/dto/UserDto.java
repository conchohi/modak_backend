package com.modak.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class UserDto {
    @JsonProperty("id")
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String profileImage;
    private String sns;
    private String role;

    private MultipartFile profileFile;
    private List<CampDto> favorites;
}
