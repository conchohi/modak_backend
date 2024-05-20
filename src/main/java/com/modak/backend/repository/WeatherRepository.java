package com.modak.backend.repository;

import com.modak.backend.entity.WeatherEntity;
import com.modak.backend.entity.embeddable.WeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherEntity, WeatherId> {
    @Query("select w from WeatherEntity w where w.weather like concat('%', :weather, '%') and w.weatherId.date = :date" )
    public List<WeatherEntity> getByWeatherAndDate(@Param("weather")String weather,@Param("date") LocalDate date);

    @Query("select w from WeatherEntity w where w.weatherId.date = :date" )
    public List<WeatherEntity> getByDate(@Param("date") LocalDate date);

    @Query("select w from WeatherEntity w where w.weatherId.region = :region and w.weatherId.date = :date")
    public WeatherEntity getCurrentWeatherByRegion(@Param("region") String region, @Param("date") LocalDate date);

    @Query("select w from WeatherEntity w where w.weatherId.region = :region")
    public List<WeatherEntity> getWeeklyWeatherByRegion(@Param("region") String region);
}
