package com.modak.backend.service.impl;

import com.modak.backend.dto.CampDto;
import com.modak.backend.dto.PageRequestDto;
import com.modak.backend.dtointerface.CampInterface;
import com.modak.backend.entity.CampEntity;
import com.modak.backend.entity.embeddable.CampFacility;
import com.modak.backend.entity.embeddable.CampType;
import com.modak.backend.repository.CampRepository;
import com.modak.backend.service.CampService;
import com.modak.backend.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CampServiceImpl implements CampService {
    private final CampRepository campRepository;
    private final WeatherService weatherService;
    private final ModelMapper modelMapper;

    @Override
    public Long register(CampDto campDto) {
        CampEntity campEntity = dtoToEntity(campDto);
        CampEntity result = campRepository.save(campEntity);
        return result.getCampNo();
    }

    @Override
    public long clear() {
        long count = campRepository.count();
        campRepository.deleteAllInBatch();
        return count;
    }

    @Override
    public CampDto get(Long campNo) {
        CampEntity campEntity = campRepository.getByCampNo(campNo);
        CampDto campDto = CampDto.builder()
                .campNo(campEntity.getCampNo())
                .name(campEntity.getName())
                .address(campEntity.getAddress())
                .intro(campEntity.getIntro())
                .lat(campEntity.getLat())
                .lon(campEntity.getLon())
                .imgName(campEntity.getImgName())
                .phone(campEntity.getPhone())
                .region(campEntity.getRegion())
                .homePage(campEntity.getHomePage())
                .build();

        campDto.setTypes(getTypes(campEntity.getCampTypes()));
        campDto.setFacilities(getFacilities(campEntity.getFacilities()));

        return campDto;
    }

    //날씨가 좋은 지역들 중 4개의 캠핑장 추천
    @Override
    public List<CampDto> getBest4(LocalDate date) {
        List<String> regions = weatherService.getRegionsByWeather("맑음",date);
        System.out.println(regions);
        List<CampDto> dtoList = new ArrayList<>();
        List<CampInterface> campInterfaceList = campRepository.selectBestFourByBestRegions(regions);
        for (CampInterface campInterface : campInterfaceList) {
            CampDto campDto = interfaceToDto(campInterface);

            dtoList.add(campDto);
        }
        return dtoList;
    }

    //해당 지역의 4개의 캠핑장
    @Override
    public List<CampDto> getBest4(String region) {
        List<CampDto> dtoList = new ArrayList<>();
        List<CampInterface> campInterfaceList = campRepository.selectBestFourByBestRegion(region);
        for (CampInterface campInterface : campInterfaceList) {
            CampDto campDto = interfaceToDto(campInterface);

            dtoList.add(campDto);
        }
        return dtoList;
    }

    @Override
    public List<CampDto> getListByRegion(PageRequestDto pageRequestDto) {
        List<CampDto> dtoList = new ArrayList<>();

        int page = pageRequestDto.getPage() - 1;
        int size = pageRequestDto.getSize();
        String type = pageRequestDto.getType();
        String region = checkNull(pageRequestDto.getRegion());
        String searchTerm = checkNull(pageRequestDto.getSearchTerm());
        Pageable pageable = PageRequest.of(page, size);

        List<CampEntity> campEntityList;

        //type 을 지정 안 할 경우 where 조건에서 제외
        if (type == null) campEntityList = campRepository.getListByRegion(pageable,region,searchTerm).getContent();
        else campEntityList = campRepository.getListByRegion(pageable,type,region,searchTerm).getContent();

        for (CampEntity campEntity : campEntityList) {
            CampDto campDto = entityToDto(campEntity);

            campDto.setTypes(getTypes(campEntity.getCampTypes()));
            dtoList.add(campDto);
        }
        return dtoList;
    }

    @Override
    public List<CampDto> getListByWeather(PageRequestDto pageRequestDto) {
        List<CampDto> dtoList = new ArrayList<>();
        int page = pageRequestDto.getPage() - 1;
        int size = pageRequestDto.getSize();
        String weather = checkNull(pageRequestDto.getWeather());
        LocalDate date = pageRequestDto.getDate();

        List<String> regions = weatherService.getRegionsByWeather(weather,date);
        String type = pageRequestDto.getType();
        String searchTerm = checkNull(pageRequestDto.getSearchTerm());
        Pageable pageable = PageRequest.of(page, size);

        List<CampEntity> campEntityList;

        //type 을 지정 안 할 경우 where 조건에서 제외
        if (type == null) campEntityList = campRepository.getListByWeather(pageable,regions,searchTerm).getContent();
        else campEntityList = campRepository.getListByWeather(pageable,type,regions,searchTerm).getContent();

        for (CampEntity campEntity : campEntityList) {
            CampDto campDto = entityToDto(campEntity);

            campDto.setTypes(getTypes(campEntity.getCampTypes()));
            dtoList.add(campDto);
        }
        return dtoList;
    }

    private CampDto interfaceToDto(CampInterface campInterface) {
        return CampDto.builder()
                .campNo(campInterface.getCampNo())
                .name(campInterface.getName())
                .address(campInterface.getAddress())
                .imgName(campInterface.getImgName())
                .build();
    }
    private CampDto entityToDto(CampEntity campEntity) {
        return CampDto.builder()
                .campNo(campEntity.getCampNo())
                .name(campEntity.getName())
                .address(campEntity.getAddress())
                .lineIntro(campEntity.getLineIntro())
                .imgName(campEntity.getImgName())
                .phone(campEntity.getPhone())
                .build();
    }

    private CampEntity dtoToEntity(CampDto campDto){
        CampEntity campEntity = CampEntity.builder()
                .name(campDto.getName())
                .intro(campDto.getIntro())
                .lineIntro(campDto.getLineIntro())
                .address(campDto.getAddress())
                .region(campDto.getRegion())
                .phone(campDto.getPhone())
                .lat(campDto.getLat())
                .lon(campDto.getLon())
                .homePage(campDto.getHomePage())
                .imgName(campDto.getImgName())
                .build();

        for (String facility : campDto.getFacilities()) {
            campEntity.addFacility(facility);
        }

        for (String type : campDto.getTypes()) {
            campEntity.addType(type);
        }

        return campEntity;
    }

    private List<String> getTypes(List<CampType> typeList){
        List<String> types = new ArrayList<>();
        for (CampType type : typeList) {
            types.add(type.getType());
        }
        return types;
    }
    private List<String> getFacilities(List<CampFacility> facilityList){
        List<String> facilities = new ArrayList<>();
        for (CampFacility facility : facilityList) {
            facilities.add(facility.getFacility());
        }

        return facilities;
    }

    private String checkNull(String str){
        return str != null ? str : "";
    }


}
