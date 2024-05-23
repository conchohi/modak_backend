package com.modak.backend.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String userId;
    private Long campNo;
    private String userNickname;
    private String userProfileImage;
    private CampDto campDto;
    private Long reviewNo;
    private String title;
    private String content;
    private double score;
    private LocalDate createDate;
}
