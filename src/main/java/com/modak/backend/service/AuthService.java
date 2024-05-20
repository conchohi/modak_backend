package com.modak.backend.service;

import com.modak.backend.dto.UserDto;
import com.modak.backend.dto.request.auth.IdCheckRequestDto;
import com.modak.backend.dto.request.auth.IdCheckResponseDto;
import com.modak.backend.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<? super ResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super ResponseDto> join(UserDto dto);
}
