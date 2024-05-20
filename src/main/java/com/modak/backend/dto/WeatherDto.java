package com.modak.backend.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WeatherDto {
    private String region;
    private LocalDate date;
    private double highTemp;
    private double lowTemp;
    private double temp;
    private String weather;
    private String day;

}
