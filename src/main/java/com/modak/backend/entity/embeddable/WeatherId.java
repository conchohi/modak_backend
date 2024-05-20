package com.modak.backend.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class WeatherId implements Serializable {
    private String region;
    private LocalDate date;
}
