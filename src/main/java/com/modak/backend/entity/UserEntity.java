package com.modak.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user")
@Table(name="user_tbl")
@ToString
public class UserEntity {
    @Id
    private String userId;
    private String password;
    private String email;
    private String role;
    private String nickname;
    private String profileImage;
    private String sns;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL
            , orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<FavoriteEntity> favorites;
}
