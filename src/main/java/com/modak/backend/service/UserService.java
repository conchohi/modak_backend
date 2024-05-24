package com.modak.backend.service;

import com.modak.backend.dto.UserDto;
import com.modak.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {
    public UserDto get(String userId);
    public void modify(UserDto userDto);
//    public void delete(String userId);
}
