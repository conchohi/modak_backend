package com.modak.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review_tbl")
@ToString(exclude = {"user","camp"})
@Entity
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_no")
    private CampEntity camp;

    private String title;
    private String content;
    private double score;
    @CreationTimestamp
    private LocalDate createDate;

    public void change(String title, String content, double score){
        this.title = title;
        this.content = content;
        this.score = score;
    }
}
