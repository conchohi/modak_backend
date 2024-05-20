package com.modak.backend.service;


import com.modak.backend.dto.WeatherDto;

import java.time.LocalDate;
import java.util.List;

public interface WeatherService {
    public void register(WeatherDto weatherDto);
    public void clear();
    public void modifyCurrentWeather(WeatherDto weatherDto);
    public void deletePastWeather();
    public List<String> getRegionsByWeather(String weather, LocalDate date);
    public List<WeatherDto> getWeatherByRegion(LocalDate date);

    public WeatherDto getCurrentWeatherByRegion(String region);
    public List<WeatherDto> getWeeklyWeatherByRegion(String region);
}
