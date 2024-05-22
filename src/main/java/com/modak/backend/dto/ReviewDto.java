package com.modak.backend.dto;

import lombok.*;

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
    private CampDto campDto;
    private Long reviewNo;
    private String title;
    private String content;
    private double score;

}
