package com.modak.backend.controller;

import com.modak.backend.dto.CampDto;
import com.modak.backend.dto.PageRequestDto;
import com.modak.backend.service.CampService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camp")
public class CampController {
    private final CampService campService;
    @GetMapping("/best")
    public ResponseEntity<?> mainBest(LocalDate date){
        List<CampDto> campDtoList = campService.getBest4(date);

        return ResponseEntity.ok(Map.of("data",campDtoList));
    }
    @GetMapping("/best-region")
    public ResponseEntity<?> mainBest(String region){
        List<CampDto> campDtoList = campService.getBest4(region);

        return ResponseEntity.ok(Map.of("data",campDtoList));
    }

    @GetMapping("/listByRegion")
    public ResponseEntity<?> listByRegion(@RequestBody PageRequestDto pageRequestDto){
        List<CampDto> campDtoList = campService.getListByRegion(pageRequestDto);

        return ResponseEntity.ok(Map.of("data",campDtoList));
    }
    //원하는 페이지, 사이즈, 날짜, 날씨
    @GetMapping("/listByWeather")
    public ResponseEntity<?> listByWeather(@RequestBody PageRequestDto pageRequestDto){
        List<CampDto> campDtoList = campService.getListByWeather(pageRequestDto);

        return ResponseEntity.ok(Map.of("data",campDtoList));
    }
    @GetMapping("/{campNo}")
    public ResponseEntity<?> campDetail(@PathVariable Long campNo){
        CampDto campDto = campService.get(campNo);

        return ResponseEntity.ok(Map.of("data",campDto));
    }

}
