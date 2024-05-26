package com.modak.backend.service.impl;

import com.modak.backend.common.CertificationNumber;
import com.modak.backend.dto.UserDto;
import com.modak.backend.dto.request.auth.CheckCertificationRequestDto;
import com.modak.backend.dto.request.auth.EmailCertificationRequestDto;
import com.modak.backend.dto.request.auth.FindIdRequestDto;
import com.modak.backend.dto.request.auth.IdCheckRequestDto;
import com.modak.backend.dto.response.auth.IdCheckResponseDto;
import com.modak.backend.dto.response.ResponseDto;
import com.modak.backend.dto.response.auth.CheckCertificationResponseDto;
import com.modak.backend.dto.response.auth.EmailCertificationResponseDto;
import com.modak.backend.entity.CertificationEntity;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.provider.EmailProvider;
import com.modak.backend.repository.CertificationRepository;
import com.modak.backend.repository.UserRepository;
import com.modak.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final EmailProvider emailProvider;
    private final CertificationRepository certificationRepository;

    @Override
    public ResponseEntity<? super ResponseDto> idCheck(IdCheckRequestDto dto) {
        try {
            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return IdCheckResponseDto.duplicateId();

        } catch (Exception exception) {
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

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String userId = dto.getId();
            String email = dto.getEmail();

            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return EmailCertificationResponseDto.duplicateId();

            String certificationNumber = CertificationNumber.getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if(!isSuccessed) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity certificationEntity = new CertificationEntity(userId, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        } catch (Exception exception) {
            return ResponseDto.databaseError();
        }

        return EmailCertificationResponseDto.success();
    }

    // 인증번호 확인
    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String userId = dto.getId();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            if(certificationEntity==null) return CheckCertificationResponseDto.certificationFail();

            boolean isMatched = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if(!isMatched) return CheckCertificationResponseDto.certificationFail();
        } catch (Exception exception) {
            return ResponseDto.databaseError();
        }
        return CheckCertificationResponseDto.success();
    }

    // 아이디 찾기
    @Override
    public ResponseEntity<? super ResponseDto> findIdByEmail(String email) {
        try {
            UserEntity user = userRepository.findByEmail(email);
            if (user != null) {
                String userIdPart = user.getUserId().substring(0, 3) + "***";
                emailProvider.sendCertificationMail(email, "회원님의 아이디는 " + userIdPart + "입니다.");
                return ResponseEntity.ok(new ResponseDto());
            } else {
                return ResponseEntity.badRequest().body(new ResponseDto("해당 이메일로 등록된 아이디가 없습니다.", email));
            }
        } catch (Exception e) {
            return ResponseDto.databaseError();
        }
    }

}

