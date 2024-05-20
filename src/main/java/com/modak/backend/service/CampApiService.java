package com.modak.backend.service;

import com.modak.backend.dto.CampDto;
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

@Service
@Transactional
@RequiredArgsConstructor
public class CampApiService {
    @Value("${goCamping-api-key}")
    private String campingApiKey;
    
    private final CampService campService;

    //매달 1일 3시에 스케줄링
    @Scheduled(cron = "0 0 3 1 * *")
    public void registerByApi(){
        int totalCount = getCampTotalCount();
        int size = 500;
        int totalPage = (int) Math.ceil(((double) totalCount / size));

        campService.clear();

        for (int page = 1; page <= totalPage; page++) {
            try {
                JSONObject jsonObject = getJsonObject(page, size);
                JSONObject response = (JSONObject) jsonObject.get("response");
                JSONObject body = (JSONObject) response.get("body");
                JSONObject itemsObject = (JSONObject) body.get("items");
                JSONArray items = (JSONArray) itemsObject.get("item");
                for(int i = 0; i < items.size(); i++){
                    JSONObject item = (JSONObject) items.get(i);
                    CampDto campDto = objectToDto(item);
                    campService.register(campDto);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private int getCampTotalCount(){
        int totalCount = 0;
        try {
            JSONObject jsonObject = (JSONObject) getJsonObject(1,1);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");

            totalCount = (int) body.getAsNumber("totalCount");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return totalCount;
    }
    private JSONObject getJsonObject(int page, int size) throws IOException, ParseException {
        URL url = new URL("http://apis.data.go.kr/B551011/GoCamping/basedList?serviceKey=" + campingApiKey + "&numOfRows=" +size + "&pageNo=" + page + "&MobileOS=ETC&MobileApp=modak&_type=json");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-type", "application/json");
        BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
        String result = bf.readLine();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        return jsonObject;
    }

    private CampDto objectToDto(JSONObject item){
        CampDto campDto = CampDto.builder()
                .name((String)item.get("facltNm"))
                .lineIntro((String)item.get("lineIntro"))
                .intro((String)item.get("intro"))
                .address((String)item.get("addr1"))
                .region((String)item.get("doNm"))
                .phone((String)item.get("tel"))
                .lat(Double.parseDouble((String)item.get("mapY")))
                .lon(Double.parseDouble((String)item.get("mapX")))
                .homePage((String)item.get("homepage"))
                .imgName((String)item.get("firstImageUrl"))
                .build();

        //시설 추가
        String facilities = (String)item.get("sbrsCl");
        for (String facility : facilities.split(",")) {
            campDto.getFacilities().add(facility);
        }

        String types = (String)item.get("induty");
        for (String type : types.split(",")){
            campDto.getTypes().add(type);
        }

        return campDto;
    }
}
