package com.aya.user_service.service;

import com.aya.user_service.mapper.UserMapper;
import com.aya.user_service.reqres.AuthenticationResponse;
import com.aya.user_service.reqres.RegisterRequest;
import com.aya.user_service.model.Role;
import com.aya.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final UserMapper userMapper;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = userMapper.fromDtoToUser(request, Role.CUSTOMER);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


}
