package com.modak.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter@Setter
@Builder@NoArgsConstructor@AllArgsConstructor
@Entity@Table(name = "favorite_tbl")
public class FavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer favoriteNo; //즐겨찾기 번호

    @ManyToOne
    @JoinColumn(name = "camp_no")
    private CampEntity camp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
