package com.modak.backend.controller;

import com.modak.backend.dto.WeatherDto;
import com.modak.backend.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<?> getAllWeather(@RequestParam("date") LocalDate date){
        List<WeatherDto> dtoList = weatherService.getWeatherByRegion(date);
        return ResponseEntity.ok(Map.of("data",dtoList));
    }

    @GetMapping("/now")
    public ResponseEntity<?> getCurrentWeather(@RequestParam("region") String region){
        WeatherDto weatherDto = weatherService.getCurrentWeatherByRegion(region);
        return ResponseEntity.ok(Map.of("data",weatherDto));
    }
    @GetMapping("/week")
    public ResponseEntity<?> getWeeklyWeather(@RequestParam("region") String region){
        List<WeatherDto> dtoList = weatherService.getWeeklyWeatherByRegion(region);
        return ResponseEntity.ok(Map.of("data",dtoList));
    }
}
