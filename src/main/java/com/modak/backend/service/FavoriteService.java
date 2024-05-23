package com.modak.backend.service;

import com.modak.backend.dto.FavoriteDto;
import com.modak.backend.entity.CampEntity;
import com.modak.backend.entity.FavoriteEntity;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.repository.CampRepository;
import com.modak.backend.repository.FavoriteRepository;
import com.modak.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CampRepository campRepository;

    public  Integer addFavorite(FavoriteDto favoriteDTO) {
        UserEntity user = userRepository.findById(favoriteDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        CampEntity camp = campRepository.findById(favoriteDTO.getCampNo()).orElseThrow(() -> new RuntimeException("Camp not found"));

        FavoriteEntity favorite = FavoriteEntity.builder()
                .user(user)
                .camp(camp)
                .build();

        FavoriteEntity savedFavorite = favoriteRepository.save(favorite);


        return savedFavorite.getFavoriteNo();
    }

    public List<FavoriteDto> getFavoritesByUser(String userId) {
        List<FavoriteEntity> favorites = favoriteRepository.findByUserUserId(userId);
        return favorites.stream().map(fav -> {
            FavoriteDto dto = new FavoriteDto();
            dto.setFavoriteNo(fav.getFavoriteNo());
            dto.setUserId(fav.getUser().getUserId());
            dto.setCampNo(fav.getCamp().getCampNo());
            return dto;
        }).collect(Collectors.toList());
    }

    public void removeFavorite(FavoriteDto favoriteDTO) {
        String userID = favoriteDTO.getUserId();
        Long campNo = favoriteDTO.getCampNo();

        // userId와 campNo를 사용하여 FavoriteEntity를 찾습니다.
        FavoriteEntity favorite = favoriteRepository.findByUserUserId(userID)
                .stream()
                .filter(fav -> fav.getCamp().getCampNo().equals(campNo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        // 조회된 FavoriteEntity의 favoriteNo를 사용하여 삭제합니다.
        favoriteRepository.deleteById(favorite.getFavoriteNo());
    }

    public boolean getFavorite(FavoriteDto favoriteDto){
        FavoriteEntity favorite = favoriteRepository.findByUserUserIdAndCampCampNo(
                favoriteDto.getUserId(), favoriteDto.getCampNo()
        );

        return favorite != null;
    }
}

