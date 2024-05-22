package com.modak.backend.service.impl;

import com.modak.backend.dto.CampDto;
import com.modak.backend.dto.PageRequestDto;
import com.modak.backend.service.CampService;
import com.modak.backend.service.WeatherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class CampServiceImplTest {
    @Autowired
    CampService campService;
    @Autowired
    WeatherService weatherService;

    @Test
    void getBest4() {
        List<CampDto> campDtoList = campService.getBest4(LocalDate.now());
        for (CampDto campDto : campDtoList) {
            System.out.println(campDto);
        }
    }
    @Test
    void getBest4ForRegion() {
        List<CampDto> campDtoList = campService.getBest4("강원도");
        for (CampDto campDto : campDtoList) {
            System.out.println(campDto);
        }
    }

    @Test
    @DisplayName("리스트")
    void getList(){
        PageRequestDto pageRequestDto = new PageRequestDto();
        pageRequestDto.setPage(1);
        pageRequestDto.setSize(5);
        pageRequestDto.setType("글램핑");
        List<CampDto> campDtoList = campService.getListByRegion(pageRequestDto).getDtoList();
        for (CampDto campDto : campDtoList) {
            System.out.println(campDto);
        }
    }

    @Test
    @DisplayName("하나")
    void get(){
        CampDto campDto = campService.get(3862L);
        System.out.println(campDto);
    }
}