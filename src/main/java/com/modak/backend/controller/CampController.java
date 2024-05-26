package com.modak.backend.controller;

import com.modak.backend.dto.CampDto;
import com.modak.backend.dto.PageRequestDto;
import com.modak.backend.dto.response.PageResponseDto;
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
    //api/camp/best?date=xxxx-xx-xx
    @GetMapping("/best")
    public ResponseEntity<?> mainBest(@RequestParam("date") LocalDate date){
        List<CampDto> campDtoList = campService.getBest4(date);

        return ResponseEntity.ok(Map.of("data",campDtoList));
    }
    //api/camp/best-region?region=xx
    @GetMapping("/best-region")
    public ResponseEntity<?> mainBest(@RequestParam("region") String region){
        List<CampDto> campDtoList = campService.getBest4(region);

        return ResponseEntity.ok(Map.of("data",campDtoList));
    }

    @GetMapping("/listByRegion")
    public ResponseEntity<?> listByRegion(PageRequestDto pageRequestDto){
        PageResponseDto<CampDto> pageResponseDto = campService.getListByRegion(pageRequestDto);

        return ResponseEntity.ok(Map.of("data",pageResponseDto));
    }
    //원하는 페이지, 사이즈, 날짜, 날씨
    //날짜와 날씨 필수여야 하므로, 프론트 단에서 기본값 지정 ex) 오늘 날짜, 맑은 날
    @GetMapping("/listByWeather")
    public ResponseEntity<?> listByWeather(PageRequestDto pageRequestDto){
        PageResponseDto<CampDto> pageResponseDto = campService.getListByWeather(pageRequestDto);

        return ResponseEntity.ok(Map.of("data",pageResponseDto));
    }
    @GetMapping("/{campNo}")
    public ResponseEntity<?> campDetail(@PathVariable(name = "campNo") Long campNo){
        CampDto campDto = campService.get(campNo);

        return ResponseEntity.ok(Map.of("data",campDto));
    }

}
