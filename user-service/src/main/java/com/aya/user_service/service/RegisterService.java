package com.aya.user_service.service;

import com.aya.user_service.exception.CannotCreateAdminException;
import com.aya.user_service.helper.UserMapConvertor;
import com.aya.user_service.mapper.UserMapper;
import com.aya.user_service.model.User;
import com.aya.user_service.reqres.AuthenticationResponse;
import com.aya.user_service.reqres.UserDto;
import com.aya.user_service.repository.UserRepository;
import com.aya.user_service.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserMapConvertor userMapConvertor;
    private final UserMapper userMapper;

    public Object register(UserDto request) throws IllegalAccessException {

        // builds a User object using HTTP request body
        var user = userMapper.fromDtoToUser(request);

        // adds user to database
        userRepository.save(user);

        // create a map to generate JWT token
        Map<String, Object> claims = userMapConvertor.convertUserToMap(user);

        // generates an encoded JWT token for the created user with extra claims
        var jwtToken = jwtUtils.generateToken(claims, user.getUsername());

        // returns the JWT token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

//    public AuthenticationResponse register(UserDto request) {
//        if(request.getRole().equals("ADMIN")) {
//            throw new CannotCreateAdminException("Cannot create an admin account, please refer to an existing admin");
//        }
//        var user = userMapper.fromDtoToUser(request);
//        userRepository.save(user);
//        var jwtToken = jwtUtils.generateToken(user);
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }


}
