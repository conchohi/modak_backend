package com.modak.backend.service.impl;

import com.modak.backend.dto.CampDto;
import com.modak.backend.dto.PageRequestDto;
import com.modak.backend.dto.ReviewDto;
import com.modak.backend.dto.response.PageResponseDto;
import com.modak.backend.dtointerface.CampInterface;
import com.modak.backend.entity.CampEntity;
import com.modak.backend.entity.ReviewEntity;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.entity.embeddable.CampFacility;
import com.modak.backend.entity.embeddable.CampType;
import com.modak.backend.repository.CampRepository;
import com.modak.backend.service.CampService;
import com.modak.backend.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

        campDto.setReviewList(getReivewList(campEntity.getReviewList()));
        campDto.setTypes(getTypes(campEntity.getCampTypes()));
        campDto.setFacilities(getFacilities(campEntity.getFacilities()));

        return campDto;
    }

    //날씨가 좋은 지역들 중 4개의 캠핑장 추천
    @Override
    public List<CampDto> getBest4(LocalDate date) {
        List<String> regions = weatherService.getRegionsByWeather("맑음",date);
        //혹시나 맑은 날씨가 없을 경우 맑음 -> 구름 -> 흐림 순으로
        if(regions.isEmpty()){
            regions = weatherService.getRegionsByWeather("구름", date);
            if(regions.isEmpty()){
                regions = weatherService.getRegionsByWeather("흐림", date);
                if(regions.isEmpty()){
                    regions = weatherService.getRegionsByWeather("", date);
                }
            }
        }

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
    public PageResponseDto<CampDto> getListByRegion(PageRequestDto pageRequestDto) {
        List<CampDto> dtoList = new ArrayList<>();

        int page = pageRequestDto.getPage() - 1;
        int size = pageRequestDto.getSize();
        String type = pageRequestDto.getType();
        String region = checkNull(pageRequestDto.getRegion());
        String searchTerm = checkNull(pageRequestDto.getSearchTerm());
        Pageable pageable = PageRequest.of(page, size);

        Page<CampEntity> campPage;

        //type 을 지정 안 할 경우 where 조건에서 제외
        if (!StringUtils.hasText(type)) campPage = campRepository.getListByRegion(pageable,region,searchTerm);
        else campPage = campRepository.getListByRegion(pageable,type,region,searchTerm);

        for (CampEntity campEntity : campPage.getContent()) {
            CampDto campDto = entityToDto(campEntity);

            campDto.setTypes(getTypes(campEntity.getCampTypes()));
            dtoList.add(campDto);
        }

        PageResponseDto<CampDto> pageResponseDto = PageResponseDto.<CampDto>builder()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDto)
                .totalCount(campPage.getTotalElements())
                .build();

        return pageResponseDto;
    }

    @Override
    public PageResponseDto<CampDto> getListByWeather(PageRequestDto pageRequestDto) {
        List<CampDto> dtoList = new ArrayList<>();
        int page = pageRequestDto.getPage() - 1;
        int size = pageRequestDto.getSize();
        String weather = checkNull(pageRequestDto.getWeather());
        LocalDate date = pageRequestDto.getDate();

        List<String> regions = weatherService.getRegionsByWeather(weather,date);
        String type = pageRequestDto.getType();
        String searchTerm = checkNull(pageRequestDto.getSearchTerm());
        Pageable pageable = PageRequest.of(page, size);


        //type 을 지정 안 할 경우 where 조건에서 제외
        Page<CampEntity> campPage;

        //type 을 지정 안 할 경우 where 조건에서 제외
        if (!StringUtils.hasText(type)) campPage
                = campRepository.getListByWeather(pageable,regions,searchTerm);
        else campPage = campRepository.getListByWeather(pageable,type,regions,searchTerm);

        for (CampEntity campEntity : campPage.getContent()) {
            CampDto campDto = entityToDto(campEntity);

            campDto.setTypes(getTypes(campEntity.getCampTypes()));
            dtoList.add(campDto);
        }

        PageResponseDto<CampDto> pageResponseDto = PageResponseDto.<CampDto>builder()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDto)
                .totalCount(campPage.getTotalElements())
                .build();

        return pageResponseDto;
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
                .reviewCount(campEntity.getReviewList().size())
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
            if(type.equals("자동차야영장")){
                type = "오토캠핑";
            } else if (type.equals("일반야영장")) {
                type = "일반캠핑장";
            }
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
    private List<ReviewDto> getReivewList(List<ReviewEntity> reviewEntityList){
        List<ReviewDto> reviewList = new ArrayList<>();
        for (ReviewEntity reviewEntity : reviewEntityList) {
            UserEntity userEntity = reviewEntity.getUser();
            ReviewDto reviewDto = ReviewDto.builder()
                    .reviewNo(reviewEntity.getReviewNo())
                    .title(reviewEntity.getTitle())
                    .content(reviewEntity.getContent())
                    .score(reviewEntity.getScore())
                    .userNickname(userEntity.getNickname())
                    .createDate(reviewEntity.getCreateDate())
                    .userProfileImage(userEntity.getProfileImage())
                    .build();

            reviewList.add(reviewDto);
        }
        return reviewList;
    }

    private String checkNull(String str){
        return str != null ? str : "";
    }


}
