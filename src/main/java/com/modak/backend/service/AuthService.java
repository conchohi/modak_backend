package com.modak.backend.service;

import com.modak.backend.dto.UserDto;
import com.modak.backend.dto.request.auth.CheckCertificationRequestDto;
import com.modak.backend.dto.request.auth.EmailCertificationRequestDto;
import com.modak.backend.dto.request.auth.IdCheckRequestDto;
import com.modak.backend.dto.response.ResponseDto;
import com.modak.backend.dto.response.auth.CheckCertificationResponseDto;
import com.modak.backend.dto.response.auth.EmailCertificationResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<? super ResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super ResponseDto> join(UserDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);

}
