package com.modak.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeDto {

    private Long noticeNo;
    private String title;
    private String content;
    private String imgName;

}
