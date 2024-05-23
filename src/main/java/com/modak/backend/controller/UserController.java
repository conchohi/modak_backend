package com.modak.backend.controller;

import com.modak.backend.dto.UserDto;
import com.modak.backend.dto.response.ResponseDto;
import com.modak.backend.dto.response.auth.IdCheckResponseDto;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.repository.UserRepository;
import com.modak.backend.service.UserService;
import com.modak.backend.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final FileUtil fileUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<?> get(){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.get(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{profileImage}")
    public ResponseEntity<Resource> getImage(@PathVariable("profileImage") String profileImage){
        return fileUtil.getFile(profileImage);
    }
    @PutMapping("")
    public ResponseEntity<?> modify(@RequestBody UserDto userDto){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        userDto.setUsername(id);
        userService.modify(userDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/join")
    public ResponseEntity<? super ResponseDto> join(@RequestBody UserDto dto) {
        try {
            String userId = dto.getUsername();
            boolean isExistId = userRepository.existsByUserId(userId);
            if (isExistId) return IdCheckResponseDto.duplicateId();

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
