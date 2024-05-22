package com.modak.backend.controller;

import com.modak.backend.dto.UserDto;
import com.modak.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @PostMapping("")
    public ResponseEntity<?> get(){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.get(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("")
    public ResponseEntity<?> modify(@RequestBody UserDto userDto){
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        userDto.setUsername(id);
        userService.modify(userDto);
        return ResponseEntity.ok(userDto);
    }
}
