package com.modak.backend.service.impl;

import com.modak.backend.dto.UserDto;
import com.modak.backend.dto.jwt.CustomUserDetails;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserId(username);
        if(user != null){
            //UserDetails 에 담아서 return 하면 AuthenticationManager 가 검증
            UserDto userDto = UserDto.builder()
                    .username(user.getUserId())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .nickname(user.getNickname())
                    .build();
            return new CustomUserDetails(userDto);
        }
        return null;
    }
}
