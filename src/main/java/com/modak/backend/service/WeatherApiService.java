package com.modak.backend.service;

import com.modak.backend.constant.GeoLocation;
import com.modak.backend.dto.WeatherDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherApiService {
    @Value("${openWeather-api-key}")
    private String openWeatherApiKey;
    private final WeatherService weatherService;
    private static List<GeoLocation> locations;

    @PostConstruct
    public void init() {
        locations = GeoLocation.getGeoList();
    }

    //매일 30분에 1시간마다
    @Scheduled(cron = "0 30 0/1 * * *")
    public void registerCurrentWeatherByApi() {
        for (GeoLocation location : locations) {
            double lat = location.getLat();
            double lon = location.getLon();

            try {
                JSONObject jsonObject = getCurrentWeatherJSONObject(lat,lon);
                JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
                JSONObject weather = (JSONObject) weatherArray.get(0);
                JSONObject main = (JSONObject) jsonObject.get("main");
                WeatherDto weatherDto = WeatherDto.builder()
                        .region(location.getRegion())
                        .date(LocalDate.now())
                        .temp(main.getAsNumber("temp").doubleValue())
                        .weather((String)weather.get("description"))
                        .build();
                weatherService.modifyCurrentWeather(weatherDto);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private JSONObject getCurrentWeatherJSONObject(double lat, double lon) throws IOException, ParseException {
        URL URL = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon
                + "&appid=" + openWeatherApiKey + "&units=metric&lang=kr"
        );
        HttpURLConnection httpURLConnection = (HttpURLConnection) URL.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-type", "application/json");
        BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
        String result = bf.readLine();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        return jsonObject;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void registerWeeklyWeatherByApi() {
        weatherService.deletePastWeather();
        int cnt = 15; //15일치
        for (GeoLocation location : locations) {
            double lat = location.getLat();
            double lon = location.getLon();

            try {
                JSONObject jsonObject = getWeeklyWeatherJSONObject(cnt,lat,lon);
                JSONArray jsonArray = (JSONArray) jsonObject.get("list");
                for (int i = 0; i < cnt; i++){
                    JSONObject data = (JSONObject) jsonArray.get(i);
                    JSONObject temp = (JSONObject) data.get("temp");
                    JSONArray weatherArray = (JSONArray) data.get("weather");
                    JSONObject weather = (JSONObject) weatherArray.get(0);

                    WeatherDto weatherDto = WeatherDto.builder()
                            .region(location.getRegion())
                            .date(LocalDate.now().plusDays(i)) //차후에 수정할 예정
                            .highTemp(temp.getAsNumber("max").doubleValue())
                            .lowTemp(temp.getAsNumber("min").doubleValue())
                            .weather((String)weather.get("description"))
                            .build();

                    weatherService.register(weatherDto);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private JSONObject getWeeklyWeatherJSONObject(int cnt, double lat, double lon) throws IOException, ParseException {
        URL URL = new URL("https://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon
                + "&cnt=" + cnt +"&appid=" + openWeatherApiKey + "&units=metric&lang=kr"
        );
        HttpURLConnection httpURLConnection = (HttpURLConnection) URL.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-type", "application/json");
        BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
        String result = bf.readLine();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        return jsonObject;
    }

}
