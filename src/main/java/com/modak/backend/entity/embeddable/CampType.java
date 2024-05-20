package com.modak.backend.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampType {
//    @Enumerated(EnumType.STRING)
    private String type;
}
