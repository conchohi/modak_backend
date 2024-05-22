package com.modak.backend.service.impl;

import com.modak.backend.dto.WeatherDto;
import com.modak.backend.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class WeatherServiceImplTest {
    @Autowired
    WeatherService weatherService;
    @Test
    void getGoodWeatherRegions() {
        List<String> regions = weatherService.getRegionsByWeather("맑음",LocalDate.now());
        System.out.println(regions);
    }

    @Test
    void getWeatherByRegion() {
        List<WeatherDto> dtoList = weatherService.getWeatherByRegion(LocalDate.now());
        System.out.println(dtoList);

    }

    @Test
    void getCurrentWeather(){
        WeatherDto weatherDto = weatherService.getCurrentWeatherByRegion("서울시");
        System.out.println(weatherDto);
    }

    @Test
    void getWeeklyWeather(){
        List<WeatherDto> dtoList = weatherService.getWeeklyWeatherByRegion("서울시");
        for (WeatherDto weatherDto : dtoList) {
            System.out.println(weatherDto);
        }
    }


}