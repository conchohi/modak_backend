package com.modak.backend.service.impl;

import com.modak.backend.dto.UserDto;
import com.modak.backend.dto.request.auth.IdCheckRequestDto;
import com.modak.backend.dto.request.auth.IdCheckResponseDto;
import com.modak.backend.dto.response.ResponseDto;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.repository.UserRepository;
import com.modak.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<? super ResponseDto> idCheck(IdCheckRequestDto dto) {
        try {
            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return IdCheckResponseDto.duplicateId();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return IdCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super ResponseDto> join(UserDto dto) {
        try {
            String userId = dto.getUsername();
            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return IdCheckResponseDto.duplicateId();

            UserEntity user = UserEntity.builder()
                    .userId(userId)
                    .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .nickname(dto.getNickname())
                    .role("USER")
                    .build();
            userRepository.save(user);

        } catch (Exception exception) {
            return ResponseDto.databaseError();
        }
        return ResponseEntity.ok(new ResponseDto());
    }

}

