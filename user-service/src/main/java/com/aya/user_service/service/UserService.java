package com.aya.user_service.service;

import com.aya.user_service.exception.InvalidUserDataException;
import com.aya.user_service.mapper.UserMapper;
import com.aya.user_service.model.Role;
import com.aya.user_service.model.User;
import com.aya.user_service.repository.UserRepository;
import com.aya.user_service.reqres.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void createSellerUser(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new InvalidUserDataException("User data cannot be null!");
        }

        User user = userMapper.fromDtoToUser(registerRequest, Role.SELLER);
        userRepository.save(user);
    }
}
