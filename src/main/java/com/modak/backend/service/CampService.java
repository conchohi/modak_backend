package com.modak.backend.service;

import com.modak.backend.dto.CampDto;
import com.modak.backend.dto.PageRequestDto;
import com.modak.backend.dto.response.PageResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface CampService {
    public Long register(CampDto campDto);
    public long clear();

    public CampDto get(Long campNo);
    public List<CampDto> getBest4(LocalDate date);
    public List<CampDto> getBest4(String region);

    public PageResponseDto<CampDto> getListByRegion(PageRequestDto pageRequestDto);
    public PageResponseDto<CampDto> getListByWeather(PageRequestDto pageRequestDto);
}
