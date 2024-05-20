package com.modak.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user")
@Table(name="user_tbl")
public class UserEntity {
    @Id
    private String userId;
    private String password;
    private String email;
    private String role;
    private String nickname;
    private String profileImage;
    private String sns;
}
