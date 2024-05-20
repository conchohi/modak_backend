package com.modak.backend.service;

import com.modak.backend.dto.CustomUserDetails;
import com.modak.backend.entity.UserEntity;
import com.modak.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserId(username);
        if(user != null){
            //UserDetails 에 담아서 return 하면 AuthenticationManager 가 검증
            return new CustomUserDetails(user);
        }
        return null;
    }
}
