package com.aya.user_service.service;

import com.aya.user_service.exception.CannotCreateAdminException;
import com.aya.user_service.mapper.UserMapper;
import com.aya.user_service.reqres.AuthenticationResponse;
import com.aya.user_service.reqres.UserDto;
import com.aya.user_service.repository.UserRepository;
import com.aya.user_service.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    public AuthenticationResponse register(UserDto request) {
        if(request.getRole().equals("ADMIN")) {
            throw new CannotCreateAdminException("Cannot create an admin account, please refer to an existing admin");
        }
        var user = userMapper.fromDtoToUser(request);
        userRepository.save(user);
        var jwtToken = jwtUtils.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


}
