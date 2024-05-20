package com.modak.backend.controller;

import com.modak.backend.dto.UserDto;
import com.modak.backend.dto.request.auth.IdCheckRequestDto;
import com.modak.backend.dto.request.auth.IdCheckResponseDto;
import com.modak.backend.dto.response.ResponseDto;
import com.modak.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck( 
        @RequestBody @Valid IdCheckRequestDto requestBody){
            ResponseEntity<? super IdCheckResponseDto>  response = authService.idCheck(requestBody);
            return response;
    }
    
    //join의 경우 json 형식으로 username, password, nickname, email 받을 수 있음
    //form 데이터로 받으려면 @RequestBody 제거
    @PostMapping("/join")
    public ResponseEntity<? super ResponseDto> join(@RequestBody UserDto userDto){
        return authService.join(userDto);
    }

}

    

