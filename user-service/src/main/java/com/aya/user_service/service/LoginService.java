package com.aya.user_service.service;

import com.aya.user_service.exception.NoUserFoundException;
import com.aya.user_service.helper.UserMapConvertor;
import com.aya.user_service.repository.UserRepository;
import com.aya.user_service.reqres.LogInRequest;
import com.aya.user_service.reqres.AuthenticationResponse;
import com.aya.user_service.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserMapConvertor userMapConvertor;

    public Object authenticate(LogInRequest request) throws IllegalAccessException {
        authenticationManager.authenticate( // authenticates that username and password from HTTP request body found in DB
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // retrieves the user from the database or returns an error if not found
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        // create a map to generate JWT token
        Map<String, Object> claims = userMapConvertor.convertUserToMap(user);

        // generates an encoded JWT token for the created user with extra claims
        var jwtToken = jwtUtils.generateToken(claims, user.getUsername());

        // generates an encoded JWT token for the authenticated user
        // var jwtToken = jwtService.generateToken(user);

        // return the JWT token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

//    public AuthenticationResponse login(LogInRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new NoUserFoundException("Email not Found!"));
//        var jwtToken = jwtUtils.generateToken(user);
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
}
