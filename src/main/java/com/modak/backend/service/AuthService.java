package com.modak.backend.service;

import com.modak.backend.dto.UserDto;
import com.modak.backend.dto.request.auth.CheckCertificationRequestDto;
import com.modak.backend.dto.request.auth.EmailCertificationRequestDto;
import com.modak.backend.dto.request.auth.FindIdRequestDto;
import com.modak.backend.dto.request.auth.IdCheckRequestDto;
import com.modak.backend.dto.response.ResponseDto;
import com.modak.backend.dto.response.auth.CheckCertificationResponseDto;
import com.modak.backend.dto.response.auth.EmailCertificationResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    // 아이디 중복 체크
    ResponseEntity<? super ResponseDto> idCheck(IdCheckRequestDto dto);
    // 회원가입
    ResponseEntity<? super ResponseDto> join(UserDto dto);
    // 이메일 인증번호 전송
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    // 이메일 인증번호 확인
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto request);
    // 아이디 찾기
    ResponseEntity<? super ResponseDto> findIdByEmail(String email);
}
