package com.aya.user_service.mapper;

import com.aya.user_service.model.Role;
import com.aya.user_service.model.User;
import com.aya.user_service.reqres.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Mapper(componentModel = "spring")
@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User fromDtoToUser(RegisterRequest registerRequest, Role role) {
        return User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
    }
}
