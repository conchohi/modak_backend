package com.modak.backend.entity;

import com.modak.backend.entity.embeddable.WeatherId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="weather_tbl")
public class WeatherEntity {
    @EmbeddedId
    private WeatherId weatherId;
    private int highTemp;
    private int lowTemp;
    @Setter
    private int temp;
    @Setter
    private String weather;
}
