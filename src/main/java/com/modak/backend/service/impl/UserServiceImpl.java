package com.modak.backend.service.impl;

import com.modak.backend.dto.UserDto;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.repository.UserRepository;
import com.modak.backend.service.UserService;
import com.modak.backend.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FileUtil fileUtil;
    @Override
    public UserDto get(String userId) {
        UserEntity user = userRepository.findByUserId(userId);

        return null;
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
