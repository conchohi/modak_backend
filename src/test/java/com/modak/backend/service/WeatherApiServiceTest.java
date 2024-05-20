package com.modak.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WeatherApiServiceTest {
    @Autowired
    WeatherApiService weatherApiService;

//    @Test
//    void registerCurrentWeatherByApi() {
//        weatherApiService.registerCurrentWeatherByApi();
//    }
    @Test
    void registerWeeklyWeatherByApi() {
        weatherApiService.registerWeeklyWeatherByApi();
        weatherApiService.registerCurrentWeatherByApi();
    }


}