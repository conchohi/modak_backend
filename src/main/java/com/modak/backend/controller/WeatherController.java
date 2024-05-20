package com.modak.backend.controller;

import com.modak.backend.dto.WeatherDto;
import com.modak.backend.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllWeather(LocalDate date){
        List<WeatherDto> dtoList = weatherService.getWeatherByRegion(date);
        return ResponseEntity.ok(Map.of("data",dtoList));
    }

    @GetMapping("/now")
    public ResponseEntity<?> getCurrentWeather(String region){
        WeatherDto weatherDto = weatherService.getCurrentWeatherByRegion(region);
        return ResponseEntity.ok(Map.of("data",weatherDto));
    }
    @GetMapping("/week")
    public ResponseEntity<?> getWeeklyWeather(String region){
        List<WeatherDto> dtoList = weatherService.getWeeklyWeatherByRegion(region);
        return ResponseEntity.ok(Map.of("data",dtoList));
    }
}
