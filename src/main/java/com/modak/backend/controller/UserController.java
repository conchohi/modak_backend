package com.modak.backend.controller;

import com.modak.backend.dto.UserDto;
import com.modak.backend.service.UserService;
import com.modak.backend.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final FileUtil fileUtil;
    @GetMapping("")
    public ResponseEntity<?> get(){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.get(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{profileImage}")
    public ResponseEntity<Resource> getImage(@PathVariable("profileImage") String profileImage){
        return fileUtil.getFile(profileImage);
    }
    @PatchMapping("")
    public ResponseEntity<?> modify(UserDto userDto){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        userDto.setUsername(id);
        userService.modify(userDto);
        return ResponseEntity.ok(userDto);
    }
}
