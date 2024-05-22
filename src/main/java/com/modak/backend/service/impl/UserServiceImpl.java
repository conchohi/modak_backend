package com.modak.backend.service.impl;

import com.modak.backend.dto.CampDto;
import com.modak.backend.dto.FavoriteDto;
import com.modak.backend.dto.UserDto;
import com.modak.backend.entity.CampEntity;
import com.modak.backend.entity.FavoriteEntity;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.repository.FavoriteRepository;
import com.modak.backend.repository.UserRepository;
import com.modak.backend.service.UserService;
import com.modak.backend.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final FileUtil fileUtil;
    @Override
    public UserDto get(String userId) {
        UserEntity user = userRepository.findUserAndFavoriteByUserId(userId);
        List<CampDto> favorites = new ArrayList<>();

        for (FavoriteEntity favorite : user.getFavorites()) {
            CampEntity camp = favorite.getCamp();
            CampDto campDto = CampDto.builder()
                    .campNo(camp.getCampNo())
                    .imgName(camp.getImgName())
                    .name(camp.getName())
                    .address(camp.getAddress())
                    .build();
            favorites.add(campDto);

            if(favorites.size()>8) break;
        }

        UserDto userDto = UserDto.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .favorites(favorites)
                .build();

        return userDto;
    }

    @Override
    public void modify(UserDto userDto) {
        UserEntity user = userRepository.findByUserId(userDto.getUsername());

        String profileImage = fileUtil.saveFile(userDto.getProfileFile());
        fileUtil.deleteFile(user.getProfileImage());
        user.setNickname(userDto.getNickname());
        user.setProfileImage(profileImage);
        user.setEmail(userDto.getEmail());

    }

//    @Override
//    public void delete(String userId) {
//
//    }
}
