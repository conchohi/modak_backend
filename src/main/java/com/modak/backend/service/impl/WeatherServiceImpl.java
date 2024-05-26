package com.modak.backend.service.impl;

import com.modak.backend.constant.GeoLocation;
import com.modak.backend.dto.WeatherDto;
import com.modak.backend.entity.WeatherEntity;
import com.modak.backend.entity.embeddable.WeatherId;
import com.modak.backend.repository.WeatherRepository;
import com.modak.backend.service.WeatherService;
import com.modak.backend.util.LocalDateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;

    @Override
    public void register(WeatherDto weatherDto) {
        WeatherEntity weatherEntity = dtoToEntity(weatherDto);
        weatherRepository.save(weatherEntity);
    }

    @Override
    public void clear() {
        weatherRepository.deleteAllInBatch();
    }

    @Override
    public void modifyCurrentWeather(WeatherDto weatherDto) {
        WeatherId weatherId = new WeatherId(weatherDto.getRegion(), weatherDto.getDate());
        WeatherEntity weatherEntity = weatherRepository.findById(weatherId).orElseThrow();

        weatherEntity.setWeather(preprocessingWeather(weatherDto.getWeather()));
        weatherEntity.setTemp((int)Math.round(weatherDto.getTemp()));
    }

    @Override
    public void deletePastWeather() {
        List<GeoLocation> locations = GeoLocation.getGeoList();
        List<WeatherId> weatherIdList = new ArrayList<>();

        for (GeoLocation location : locations) {
            WeatherId weatherId = new WeatherId(location.getRegion(), LocalDate.now().minusDays(1));
            weatherIdList.add(weatherId);
        }
        weatherRepository.deleteAllByIdInBatch(weatherIdList);

    }

    //해당 일자의 좋은 날씨인 지역 리스트 가져오기
    @Override
    public List<String> getRegionsByWeather(String weather,LocalDate date) {
        List<String> regions = new ArrayList<>();
        List<WeatherEntity> weatherEntities = weatherRepository.getByWeatherAndDate(weather,date);
        for (WeatherEntity weatherEntity : weatherEntities) {
            regions.add(weatherEntity.getWeatherId().getRegion());
        }
        return regions;
    }

    //해당 일자의 지역별 날씨 가져오기
    @Override
    public List<WeatherDto> getWeatherByRegion(LocalDate date) {
        List<WeatherDto> dtoList = new ArrayList<>();
        List<WeatherEntity> weatherEntities = weatherRepository.getByDate(date);
        for (WeatherEntity weatherEntity : weatherEntities) {
            WeatherDto weatherDto = WeatherDto.builder()
                    .region(weatherEntity.getWeatherId().getRegion())
                    .weather(weatherEntity.getWeather())
                    .build();
            dtoList.add(weatherDto);
        }

        return dtoList;
    }

    @Override
    public WeatherDto getCurrentWeatherByRegion(String region) {
        LocalDate now = LocalDate.now();

        WeatherEntity weatherEntity = weatherRepository.getCurrentWeatherByRegion(region, now);
        WeatherDto weatherDto = WeatherDto.builder()
                .weather(weatherEntity.getWeather())
                .lowTemp(weatherEntity.getLowTemp())
                .highTemp(weatherEntity.getHighTemp())
                .temp(weatherEntity.getTemp())
                .day(LocalDateUtil.getDayOfWeek(now))
                .date(now)
                .build();
        return weatherDto;
    }

    @Override
    public List<WeatherDto> getWeeklyWeatherByRegion(String region) {
        List<WeatherDto> dtoList = new ArrayList<>();
        List<WeatherEntity> weatherEntityList = weatherRepository.getWeeklyWeatherByRegion(region);
        for (int i = 0; i < weatherEntityList.size(); i++) {
            WeatherEntity weatherEntity = weatherEntityList.get(i);
            LocalDate date = LocalDate.now().plusDays(i);
            WeatherDto weatherDto = WeatherDto.builder()
                    .weather(weatherEntity.getWeather())
                    .highTemp(weatherEntity.getHighTemp())
                    .lowTemp(weatherEntity.getLowTemp())
                    .day(LocalDateUtil.getDayOfWeek(date))
                    .build();
            dtoList.add(weatherDto);
        }

        return dtoList;
    }

    private WeatherEntity dtoToEntity(WeatherDto weatherDto){
        WeatherId weatherId = new WeatherId(weatherDto.getRegion(), weatherDto.getDate());

        WeatherEntity currentWeather = WeatherEntity.builder()
                .weatherId(weatherId)
                .weather(preprocessingWeather(weatherDto.getWeather()))
                .highTemp((int)Math.round(weatherDto.getHighTemp()))
                .lowTemp((int)Math.round(weatherDto.getLowTemp()))
                .build();

        return currentWeather;
    }

    private String preprocessingWeather(String weather){
        if(weather.contains("비")){
            return "비";
        } else if(weather.contains("흐림")){
            return "흐림";
        } else if(weather.contains("구름")){
            return "구름";
        } else if(weather.contains("눈")){
            return "눈";
        }
        return weather;
    }

}
